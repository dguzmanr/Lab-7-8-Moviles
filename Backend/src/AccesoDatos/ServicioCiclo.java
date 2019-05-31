/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AccesoDatos;


import Entidades.Ciclo;
import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import oracle.jdbc.OracleTypes;

/**
 *
 * @author David Guzman
 */
public class ServicioCiclo extends Servicio{
    
    private static final String INSERTARCICLO= "{call insertarCiclo(?,?,?,?,?,?)}";
    private static final String BUSCARANO = "{?=call buscarAnoCiclo(?)}";
    private static final String MODIFICARCICLO= "{call modificarCiclo(?,?,?,?,?,?)}";
    private static final String LISTAR = "{?=call listarCiclos()}";
    private static final String ELIMINARCICLO= "{call eliminarCiclo(?)}";
    private static final String BUSCARCODIGOC = "{?=call buscarCodigoCiclo(?)}";
    private static final String BUSCARCICLO = "{?=call buscarCiclo(?)}";
    
    
    public ServicioCiclo() {
        
    }
    
    public Ciclo ciclo(ResultSet rs) throws GlobalException, NoDataException{
        try {
            conectar();
        } catch (ClassNotFoundException e) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException e) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }try {
            Ciclo c= new Ciclo();
            c.setCodigo(rs.getString("COD_CICLO"));
            c.setAno(rs.getString("ANO"));
            c.setEstado(rs.getString("ESTADO").charAt(0));
            c.setNumero(rs.getString("NUMERO").charAt(0));
            c.setFecha_inicio(rs.getString("FECHA_INICIO"));
            c.setFecha_finalizacion(rs.getString("FECHA_FINALIZACION"));
            return c;
        } catch (SQLException ex) {
            return null;
        }
    }
    
    public void insertarCiclo(Ciclo ciclo) throws GlobalException, NoDataException, ParseException{

        try {
            conectar();
        } catch (ClassNotFoundException e) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException e) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }
        CallableStatement pstmt=null;
        
        try {
            pstmt = conexion.prepareCall(INSERTARCICLO);
            pstmt.setString(1,ciclo.getCodigo());
            pstmt.setString(2,ciclo.getAno());
                String aux="";
                aux+=ciclo.getEstado();     
            pstmt.setString(3, aux);
                String aux2="";
                aux2+=ciclo.getNumero();     
            pstmt.setString(4,aux2);
            pstmt.setString(5,ciclo.getFecha_inicio());
            pstmt.setString(6,ciclo.getFecha_finalizacion());
            
            
            boolean resultado = pstmt.execute();
            if (resultado == true) {
                throw new NoDataException("No se realizo la insercion");
            }
            
            } catch (SQLException e) {
                e.printStackTrace();
                throw new GlobalException("Llave duplicada");
            } finally {
                try {
                    if (pstmt != null) {
                        pstmt.close();
                    }
                    desconectar();
                } catch (SQLException e) {
                    throw new GlobalException("Estatutos invalidos o nulos");
                }
            }
        }
    
    public ArrayList listarCiclos() throws GlobalException 
    {
        try 
        {
            conectar();
        } 
        catch (ClassNotFoundException e) 
        {
            try{
                throw new GlobalException("No se ha localizado el driver.");
            } 
            catch (GlobalException ex)
            {
                Logger.getLogger(ServicioProfesor.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
        catch (SQLException e) 
        {
            try {
                throw new GlobalException("La base de datos no se encuentra disponible.");
            } catch (GlobalException ex) {
                Logger.getLogger(ServicioProfesor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        CallableStatement pstmt=null;
        ResultSet rs = null;
	ArrayList coleccion = new ArrayList();
	Ciclo elciclo = null;
        try
        {
                pstmt = conexion.prepareCall(LISTAR);
                pstmt.registerOutParameter(1, OracleTypes.CURSOR);
                pstmt.execute();
                rs = (ResultSet)pstmt.getObject(1);
                while (rs.next())
                {
                        	elciclo = new Ciclo(
                                        rs.getString("COD_CICLO"),
                                        rs.getString("ANO"),
                                        rs.getString("ESTADO").charAt(0),                                         
                                        rs.getString("NUMERO").charAt(0),
                                        rs.getString("FECHA_INICIO"),
                                        rs.getString("FECHA_FINALIZACION"));
				coleccion.add(elciclo);
                }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            try {
                throw new GlobalException("Error al recuperar datos.\n");
            } catch (GlobalException ex) {
                Logger.getLogger(ServicioProfesor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        finally
        {
            try
            {
                if (rs != null)
                {
                    rs.close();
                }
                if (pstmt != null)
                {
                    pstmt.close();
                }
                desconectar();
            }
            catch (SQLException e)
            {
                try {
                    throw new GlobalException("Estatutos invalidos o nulos.");
                } catch (GlobalException ex) {
                    Logger.getLogger(ServicioProfesor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        if (coleccion == null || coleccion.isEmpty())
        {
            try {
                throw new GlobalException("No hay datos.");
            } catch (GlobalException ex) {
                Logger.getLogger(ServicioProfesor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return coleccion;
        
    }
    
    public Ciclo buscarCiclo(String id) throws GlobalException, NoDataException {
        try {
            conectar();
        }catch (ClassNotFoundException ex){
            throw new GlobalException("No se ha localizado el driver");
        }catch (SQLException ex){
            throw new NoDataException("La base de datos no se encuentra disponible");
        }
        ResultSet rs = null;
        Ciclo elciclo = null;

        CallableStatement pstmt= null;
        try {
            
                pstmt = conexion.prepareCall(BUSCARCICLO);
                pstmt.registerOutParameter(1, OracleTypes.CURSOR);  
                pstmt.setString(2,id);
                pstmt.execute();
                rs = (ResultSet)pstmt.getObject(1);
                while (rs.next()) {
                    elciclo = new Ciclo(
                                        rs.getString("COD_CICLO"),
                                        rs.getString("ANO"),
                                        rs.getString("ESTADO").charAt(0),
                                        rs.getString("NUMERO").charAt(0),
                                        rs.getString("FECHA_INICIO"),
                                        rs.getString("FECHA_FINALIZACION")
                                    );
                }
        }
        catch (SQLException ex) {
            throw new GlobalException("Sentencia no valida");
        }
        finally {
            try {
                if (rs!=null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                desconectar();
            }
            catch(SQLException e)
            {
                throw new GlobalException("Estatutos invalidos o nulos");
            }
        }
        return elciclo;
    
    }
    
    public ArrayList buscarCodigoCiclo(String codigo) throws GlobalException, NoDataException{

		try{
			conectar();
		}catch (ClassNotFoundException e){
			throw new GlobalException("No se ha localizado el driver");
		}
		catch (SQLException e){
			throw new NoDataException("La base de datos no se encuentra disponible");
		}
                CallableStatement pstmt=null;
                ResultSet rs = null;
                ArrayList coleccion = new ArrayList();
                Ciclo elciclo = null;
               
		try{
			pstmt = conexion.prepareCall(BUSCARCODIGOC);
			pstmt.registerOutParameter(1, OracleTypes.CURSOR);
			pstmt.setString(2, codigo);
			pstmt.execute();
			rs = (ResultSet)pstmt.getObject(1);
			while (rs.next()){
				elciclo = new Ciclo(
                                        rs.getString("COD_CICLO"),
                                        rs.getString("ANO"),
                                        rs.getString("ESTADO").charAt(0),
                                        rs.getString("NUMERO").charAt(0),
                                        rs.getString("FECHA_INICIO"),
                                        rs.getString("FECHA_FINALIZACION"));
				coleccion.add(elciclo);
			}
		}
		catch (SQLException e){
			e.printStackTrace();

			throw new GlobalException("Sentencia no valida");
		}
		finally{
			try{
				if (rs != null){
					rs.close();
				}
				if (pstmt != null){
					pstmt.close();
				}
				desconectar();
			}
			catch (SQLException e){
				throw new GlobalException("Estatutos invalidos o nulos");
			}
		}
		if (coleccion == null || coleccion.size() == 0){
			throw new NoDataException("No hay datos");
		}
		return coleccion;
	}
    
    public ArrayList buscarCicloAno(String ano) throws GlobalException, NoDataException{

		try{
			conectar();
		}catch (ClassNotFoundException e){
			throw new GlobalException("No se ha localizado el driver");
		}
		catch (SQLException e){
			throw new NoDataException("La base de datos no se encuentra disponible");
		}
		ResultSet rs = null;
		ArrayList coleccion = new ArrayList();
                Ciclo elciclo = new Ciclo("","",' ',' ',null,null);        
		//Ciclo elciclo = null;
		CallableStatement pstmt = null;
		try{
			pstmt = conexion.prepareCall(BUSCARANO);
			pstmt.registerOutParameter(1, OracleTypes.CURSOR);
			pstmt.setString(2, ano);
			pstmt.execute();
			rs = (ResultSet)pstmt.getObject(1);
			while (rs.next()){
				elciclo = new Ciclo(
                                        rs.getString("COD_CICLO"),
                                        rs.getString("ANO"),
                                        rs.getString("ESTADO").charAt(0),   
                                        rs.getString("NUMERO").charAt(0),
                                        rs.getString("FECHA_INICIO"),
                                        rs.getString("FECHA_FINALIZACION"));
				coleccion.add(elciclo);
			}
		}
		catch (SQLException e){
			e.printStackTrace();

			throw new GlobalException("Sentencia no valida");
		}
		finally{
			try{
				if (rs != null){
					rs.close();
				}
				if (pstmt != null){
					pstmt.close();
				}
				desconectar();
			}
			catch (SQLException e){
				throw new GlobalException("Estatutos invalidos o nulos");
			}
		}
		if (coleccion == null || coleccion.size() == 0){
			throw new NoDataException("No hay datos");
		}
		return coleccion;
	}
    
    public boolean modificarCiclo(Ciclo elciclo) throws GlobalException,SQLException
    {
        try 
        {
            conectar();
        } 
        catch (ClassNotFoundException e) 
        {
            throw new GlobalException("No se ha localizado el driver.");
        } 
        catch (SQLException e) 
        {
            throw new GlobalException("La base de datos no se encuentra disponible.");
        }
        CallableStatement pstmt=null;
        
        try 
        {
            pstmt = conexion.prepareCall(MODIFICARCICLO);
            pstmt.setString(1, elciclo.getCodigo());
            pstmt.setString(2, elciclo.getAno());
            String aux="";
            aux+=elciclo.getEstado();     
            pstmt.setString(3, aux);
            String aux2="";
            aux2+=elciclo.getNumero();     
            pstmt.setString(4,aux2);
            pstmt.setString(5, elciclo.getFecha_inicio());
            pstmt.setString(6,elciclo.getFecha_finalizacion());
          
            
            boolean resultado = pstmt.execute();
            if (resultado == true) 
            {
                throw new GlobalException("No se pudo insertar el alumno.");
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
            throw new GlobalException("Número de identificación duplicado.");
        } 
        finally 
        {
            try 
            {
                if (pstmt != null) 
                {
                    pstmt.close();
                }
                desconectar();
                
            } 
            catch (SQLException e) 
            {
                throw new GlobalException("Error al desconectar.");
            }
            return true;
        } 
    }
    
    public boolean eliminarCiclo(String codigo) throws GlobalException,SQLException  	
    {
        try 
        {
            conectar();
        } 
        catch (ClassNotFoundException e) 
        {
            throw new GlobalException("No se ha localizado el driver.");
        } 
        catch (SQLException e) 
        {
            throw new GlobalException("La base de datos no se encuentra disponible.");
        }
        CallableStatement pstmt=null;
        
        try 
        {
            pstmt = conexion.prepareCall(ELIMINARCICLO);
            pstmt.setString(1,codigo);
            
            boolean resultado = pstmt.execute();
            if (resultado == true) 
            {
                throw new GlobalException("No se pudo insertar el ciclo.");
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
            throw new GlobalException("Número de identificación duplicado.");
        } 
        finally 
        {
            try 
            {
                if (pstmt != null) 
                {
                    pstmt.close();
                }
                desconectar();
            } 
            catch (SQLException e) 
            {
                throw new GlobalException("Error al desconectar.");
            }
            return true;
        }     
    }
}


   