/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 *
 * @author JITHZZ
 */
@MessageDriven(mappedName = "jms/Queue")
public class ProductListener implements MessageListener{
  ProductList list;
    @Override
    public void onMessage(Message message) {
        
            throw new UnsupportedOperationException("Error with JMS");
        
    }
}
