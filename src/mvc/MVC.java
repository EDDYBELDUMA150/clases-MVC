/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package mvc;

import controler.ControlerPersona;
import model.ModelPersona;
import view.ViewPersona;

/**
 *
 * @author OWNER
 */
public class MVC {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        //instanciamos en modelo y la vista y el controlador
       ViewPersona vista = new ViewPersona();
       ModelPersona persona = new ModelPersona();
       ControlerPersona controlador= new ControlerPersona(persona, vista);
       controlador.iniciaControl();
    }
    
}