package br.com.blend.dal;

import com.intelligt.modbus.jlibmodbus.Modbus;
import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusNumberException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusProtocolException;
import com.intelligt.modbus.jlibmodbus.master.ModbusMasterFactory;
import com.intelligt.modbus.jlibmodbus.master.ModbusMaster;
import com.intelligt.modbus.jlibmodbus.tcp.TcpParameters;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class modbus {
    public String host;
    public int endEscravo;
    
    public int[] lerHr(int offset, int quantity){
    
        TcpParameters tcpParameters = new TcpParameters();
        int[] registerValues = null;
        int[] aux={225};
        
        if(offset <= 15000){
            try{
                tcpParameters.setHost(InetAddress.getByName(host));
            }
            catch(UnknownHostException ex){
                Logger.getLogger(modbus.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            tcpParameters.setKeepAlive(true);
            tcpParameters.setPort(Modbus.TCP_PORT);
            
            
            ModbusMaster m = ModbusMasterFactory.createModbusMasterTCP(tcpParameters);
            Modbus.setAutoIncrementTransactionId(true);
            
            if(!m.isConnected()){
                try{
                    m.connect();
                }
                catch(ModbusIOException ex){
                    Logger.getLogger(modbus.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            try {
                registerValues = m.readHoldingRegisters(endEscravo, quantity, quantity);
            }
            catch (ModbusProtocolException | ModbusNumberException | ModbusIOException ex) {
                Logger.getLogger(modbus.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        else{
            System.out.println("numero maior que 15k");
        }
        
        return aux;
    }
}
