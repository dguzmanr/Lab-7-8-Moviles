package com.example.mobilecontrol.Activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.mobilecontrol.Adapter.AlumnoAdapter;
import com.example.mobilecontrol.Adapter.CicloAdapter;
import com.example.mobilecontrol.Helper.RecyclerItemTouchHelper;
import com.example.mobilecontrol.LogicaNegocio.Alumno;
import com.example.mobilecontrol.LogicaNegocio.Ciclo;
import com.example.mobilecontrol.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AdmCicloActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener, CicloAdapter.CicloAdapterListener {

    private RecyclerView mRecyclerView;
    private CicloAdapter mAdapter;
    private List<Ciclo> cicloList;
    private CoordinatorLayout coordinatorLayout;
    private SearchView searchView;
    private FloatingActionButton fab;

   // String apiUrl = "http://192.168.0.18:8080/BE-LabConnection/ServiceCiclo?";
    //String apiUrl ="http://10.20.105.143:8080/BE-LabConnection/ServiceCiclo?";
    String apiUrl ="http://10.20.106.109:8080/BE-LabConnection/ServiceCiclo?";
    String apiUrlTemporal = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm_ciclo);
        Toolbar toolbar = findViewById(R.id.toolbarC);
        setSupportActionBar(toolbar);

        //toolbar fancy stuff
        //getSupportActionBar().setTitle(getString(R.string.my_ciclo));

        mRecyclerView = findViewById(R.id.recycler_cicloFld);
        cicloList = new ArrayList<>();
        mAdapter = new CicloAdapter(cicloList, this);
        coordinatorLayout = findViewById(R.id.coordinator_layoutC);

        // white background notification bar
        whiteNotificationBar(mRecyclerView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);

        apiUrlTemporal = apiUrl + "opc=1";
        MyAsyncTasks myAsyncTasks = new MyAsyncTasks();
        myAsyncTasks.execute();

        // go to update or add career
        fab = findViewById(R.id.addBtnC);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAddUpdCiclo();
            }
        });

        //delete swiping left and right
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);

        //should use database info


        // Receive the Carrera sent by AddUpdCarreraActivity
        checkIntentInformation();

        //refresh view
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (direction == ItemTouchHelper.START) {
            if (viewHolder instanceof CicloAdapter.MyViewHolder) {
                // get the removed item name to display it in snack bar
                String cod = cicloList.get(viewHolder.getAdapterPosition()).getCodigo();

                apiUrlTemporal = apiUrl + "opc=3&codigo="+cod;
                MyAsyncTasks myAsyncTasks = new MyAsyncTasks();
                myAsyncTasks.execute();
                // save the index deleted
                final int deletedIndex = viewHolder.getAdapterPosition();
                // remove the item from recyclerView
                mAdapter.removeItem(viewHolder.getAdapterPosition());

                // showing snack bar with Undo option
                Snackbar snackbar = Snackbar.make(coordinatorLayout, cod + " removido!", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // undo is selected, restore the deleted item from adapter
                        mAdapter.restoreItem(deletedIndex);
                    }
                });
                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();
            }
        } else {
            //If is editing a row object
            Ciclo aux = mAdapter.getSwipedItem(viewHolder.getAdapterPosition());
            //send data to Edit Activity
            Intent intent = new Intent(this, AddUpdCicloActivity.class);
            intent.putExtra("editable", true);
            intent.putExtra("ciclo", aux);
            mAdapter.notifyDataSetChanged(); //restart left swipe view
            startActivity(intent);
        }
    }

    public class MyAsyncTasks extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... params) {

            // implement API in background and store the response in current variable
            String current = "";
            try {
                URL url;
                HttpURLConnection urlConnection = null;
                try {
                    url = new URL(apiUrlTemporal);

                    urlConnection = (HttpURLConnection) url
                            .openConnection();

                    InputStream in = urlConnection.getInputStream();

                    InputStreamReader isw = new InputStreamReader(in);

                    int data = isw.read();
                    while (data != -1) {
                        current += (char) data;
                        data = isw.read();

                    }
                    // return the data to onPostExecute method
                    Log.w("JSON", current);
                    return current;


                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
            return current;
        }


        @Override
        protected void onPostExecute(String s) {
            //S tiene la lista Actualizada que recibe del web service
            //Se actualiza el recycler view
            try {
                Gson gson = new Gson();
                ArrayList<Ciclo> CicloList= (ArrayList<Ciclo>) gson.fromJson(s,
                        new TypeToken<ArrayList<Ciclo>>() {
                        }.getType());


                cicloList = CicloList;
                mAdapter = new CicloAdapter(cicloList, AdmCicloActivity.this);
                coordinatorLayout = findViewById(R.id.coordinator_layoutAlumno);

                // white background notification bar
                whiteNotificationBar(mRecyclerView);

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                mRecyclerView.addItemDecoration(new DividerItemDecoration(AdmCicloActivity.this, DividerItemDecoration.VERTICAL));
                mRecyclerView.setAdapter(mAdapter);


                //txtView.setText(ClienteList.toString());

            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onItemMove(int source, int target) {
        mAdapter.onItemMove(source, target);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds cicloList to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);

        // Associate searchable configuration with the SearchView   !IMPORTANT
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change, every type on input
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                mAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        Intent a = new Intent(this, MainActivity.class);
        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(a);
        super.onBackPressed();
    }

    private void whiteNotificationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.WHITE);
        }
    }

    @Override
    public void onContactSelected(Ciclo ciclo) { //TODO get the select item of recycleView
        Toast.makeText(getApplicationContext(), "Selected: " + ciclo.getCodigo() + ", " + ciclo.getAno(), Toast.LENGTH_LONG).show();
    }

    private void checkIntentInformation() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Ciclo aux;
            aux = (Ciclo) getIntent().getSerializableExtra("addCiclo");
            if (aux == null) {
                aux = (Ciclo) getIntent().getSerializableExtra("editCiclo");
                if (aux != null) {
                    apiUrlTemporal = apiUrl + "opc=4&codigo="+aux.getCodigo()+"&ano="+aux.getAno()+"&estado="+aux.getEstado()+"&numero="+aux.getNumero()+"&f_inicio="+aux.getFecha_inicio()+"&f_final="+aux.getFecha_finalizacion();
                    MyAsyncTasks myAsyncTasks = new MyAsyncTasks();
                    myAsyncTasks.execute();
                    Toast.makeText(getApplicationContext(), aux.getCodigo() + " editado correctamente", Toast.LENGTH_LONG).show();
                }
            }else{
                apiUrlTemporal = apiUrl + "opc=2&codigo="+aux.getCodigo()+"&ano="+aux.getAno()+"&estado="+aux.getEstado()+"&numero="+aux.getNumero()+"&f_inicio="+aux.getFecha_inicio()+"&f_final="+aux.getFecha_finalizacion();
                MyAsyncTasks myAsyncTasks = new MyAsyncTasks();
                myAsyncTasks.execute();
                Toast.makeText(getApplicationContext(), aux.getCodigo() + " agregado correctamente", Toast.LENGTH_LONG).show();

            }
        }
    }
    private void goToAddUpdCiclo() {
        Intent intent = new Intent(this, AddUpdCicloActivity.class);
        intent.putExtra("editable", false);
        startActivity(intent);
    }



}
