/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.software2.examples.pharmacysapp;
import com.software2.examples.pharmacysapp.Producto;
import com.software2.examples.pharmacysapp.DetallePedido;
import com.software2.examples.pharmacysapp.Pago;  
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Stephany
 */

/**
 * Los pedidos a domicilio no pueden exceder de 3 productos. 
 * Por cada producto, sólo se pueden solicitar hasta un máximo de 5 unidades.
 * la app será habilitada en: Urdesa, Av. Quito y Riocentro sur
 * Norte: 8am a 11pm
 * Otros: 8am a 9pm
 * Si la compra es menor que $10, recargo en centro y sur son de $2 y $4 para norte
 * 
*/
public class PedidoTest {
    
    private static ArrayList<Producto> catalogo;
    private static ArrayList<DetallePedido> carrito;
    
    public static void initCatalogo() {
               
        catalogo = new ArrayList<Producto>();
        carrito = new ArrayList<DetallePedido>();
        //producto: String nombre, String descripcion, int stock, int limite, double precio
        Producto producto1 = new Producto("Dicloflenaco","Medicina",4,1,10.5);
        Producto producto2 = new Producto("Buscapina","Medicina",20,5,5.6);
        Producto producto3 = new Producto("Analgan","Medicina",10,2,2.50);
        Producto producto4 = new Producto("Redoxon","Medicina",30,5,7.80);
        catalogo.add(producto1);
        catalogo.add(producto2);
        catalogo.add(producto3);
        catalogo.add(producto4);
    }    
    
    public Producto obtener_producto_catalogo(ArrayList<Producto> productos, String clave){
        Producto encontrado = null;
        
        for (Producto producto : productos) {
             if(producto.getNombre().equals(clave))
                 encontrado = producto;
            }
        return encontrado;
    
    }
    
    @Before
    public void beforeEachTest() {
        initCatalogo();
    }

    @After
    public void afterEachTest() {
      
    }

    @Test
    public void testIntegracion_producto_catalogo() {
        System.out.println("----Test 1----"); 
        System.out.println("\n(INICIAL) El catalogo de la farmacia es: " + catalogo.size());
        System.out.println(catalogo.toString());
        Producto producto5 = new Producto("Ciprofloxacina","Medicina",50,15,11.42);
        String resultado = producto5.crear_producto();
        catalogo.add(producto5);
        System.out.println("\n(FIN) El catalogo de la farmacia es: " + catalogo.size());
        System.out.println(catalogo.toString()); 
        
        assertEquals("Se creo el producto exitosamente!", resultado); //experado,obtenido
        
        System.out.println("----Test 1----\n"); 
    }
    
    @Test  
    public void testIntegracion_catalogo_carrito() {
        System.out.println("----Test 2----");        
        Producto p = obtener_producto_catalogo(catalogo,"Redoxon"); //simulo que lo agrego al carrito
        DetallePedido detalle1 = new DetallePedido(p,5); //Simulo que selecciono 5
        carrito.add(detalle1);
        //visualizar el pedido con los productos seleccionados.
        System.out.println("*******Carrito*****");
        System.out.println(carrito.toString());
        assertEquals(1, carrito.size()); //experado,obtenido
        System.out.println("----Test 2----\n"); 
    }
    
   
    
    @Test  
    public void testIntegracion_PagoSubtotal() {
        System.out.println("----Test 3----");      
        Producto p1 = obtener_producto_catalogo(catalogo,"Analgan");
        Producto p2 = obtener_producto_catalogo(catalogo,"Buscapina");
        DetallePedido detalle1 = new DetallePedido(p1,1);
        DetallePedido detalle2 = new DetallePedido(p2,1);
        carrito.add(detalle1);
        carrito.add(detalle2);
        //visualizar el pedido con los productos seleccionados.
        System.out.println(carrito.toString());
        Pago pa=new Pago();
        //Ingresa el tipo de pago
        pa.crear_pago(true,"");
        Cliente client=new Cliente("Juan", 1, pa);
        System.out.println(client.InfoPer());
        System.out.println( pa.validar_pago(pa));
        double subt=detalle1.subtotal +detalle2.subtotal;
        System.out.println("El subtotal a pagar es: " + subt);
        assertEquals(true, pa.getTipo());//experado,obtenido
        System.out.println("----Test 3----\n");      
    }
    
    @Test
    public void testIntegracion_confirmarPedido(){
        Date tiempo = new Date();   //esta variable me devolverá el tiempo actual
        System.out.println("---Test 4---");
        Producto p1 = obtener_producto_catalogo(catalogo,"Analgan");
        Producto p2 = obtener_producto_catalogo(catalogo,"Diclofenaco");
        DetallePedido detalle1 = new DetallePedido(p1,1);
        DetallePedido detalle2 = new DetallePedido(p2,1);
        carrito.add(detalle1);
        carrito.add(detalle2);
        //visualizar el pedido con los productos seleccionados.
//        System.out.println(carrito.toString());
        //Seccion de tipo de pago
        Pago pago = new Pago();
        pago.crear_pago(true, "");  //Dado que es un pago en efectivo
        Cliente client = new Cliente("Kerly", 2, pago); //Ingresamos al cliente
        //Pedido(ArrayList<DetallePedido> pedido,Date hora,Cliente cliente)
        System.out.println(client.InfoPer());
        Pedido pedido = new Pedido(carrito, tiempo, client);
        System.out.println(pedido.ValidHora()); //Imprimo si la hora es o no valida
        //Si la hora actual es antes de las 9pm y luego de las 8am
        assertEquals("Horario disponible", pedido.ValidHora() );//experado,obtenido
    }
    
    @Test
    public void testIntegracion_obtenerTotalAPagar(){
        Date tiempo = new Date();
        System.out.println("---Test 5---");
        Producto p1 = obtener_producto_catalogo(catalogo,"Analgan");
        Producto p2 = obtener_producto_catalogo(catalogo,"Diclofenaco");
        DetallePedido detalle1 = new DetallePedido(p1,1);
        DetallePedido detalle2 = new DetallePedido(p2,1);
        carrito.add(detalle1);
        carrito.add(detalle2);
        Pago pago = new Pago();
        pago.crear_pago(true, "");  //Dado que es un pago en efectivo
        Cliente client = new Cliente("Kerly", 2, pago); //Ingresamos al cliente
        System.out.println(client.InfoPer());
        Pedido pedido = new Pedido(carrito, tiempo, client);
        //obtengo el recargo de los dos productos
        double recargo1 = pedido.GetRecargo(client, detalle1.SubtotalDeProducto()); //devuelve 2
        assertEquals("2.0" , ""+recargo1 );//experado,obtenido
        double recargo2 = pedido.GetRecargo(client, detalle2.SubtotalDeProducto()); // devuelve 2
        assertEquals("2.0", ""+recargo2);
        System.out.println("Subtotal a pagar" + (recargo1 + recargo2) ); //Muestro el valor del subtotal a pagar
        //13 dolares a pagar + $4 del subtotal... 17(?
        System.out.println("total del pedido: " + pedido.TotalPedido());        
        assertEquals("17.0", pedido.TotalPedido() );
    }
}
    

