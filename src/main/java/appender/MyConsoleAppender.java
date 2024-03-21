/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appender;

import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Core;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

/**
 *
 * @author keita
 */
@Plugin(
  name = "MyConsoleAppender", 
  category = Core.CATEGORY_NAME, 
  elementType = Appender.ELEMENT_TYPE)
public class MyConsoleAppender extends AbstractAppender{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }

    protected MyConsoleAppender(String name, Filter filter) {
        super(name, filter, null);
    }
    
    @PluginFactory
    public static MyConsoleAppender createAppender(
      @PluginAttribute("name") String name, 
      @PluginElement("Filter") Filter filter) {
        return new MyConsoleAppender(name, filter);
    }
    
    @Override
    public void append(LogEvent le) {
        
        System.out.format("%-80s ",le.getMessage().getFormattedMessage());
        System.out.println(le.getLevel().toString()+"  "+le.getLoggerName()+" line:"+le.getSource().getLineNumber() );
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
