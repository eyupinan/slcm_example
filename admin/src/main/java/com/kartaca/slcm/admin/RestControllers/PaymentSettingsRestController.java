package com.kartaca.slcm.admin.RestControllers;

import com.kartaca.slcm.admin.Exceptions.PasswordException;
import com.kartaca.slcm.data.model.postgresql.IyzicoModel;
import com.kartaca.slcm.data.model.postgresql.MasterpassModel;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.util.Enumeration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.kartaca.slcm.data.repository.postgresql.IyzicoConfigRepository;
import com.kartaca.slcm.data.repository.postgresql.MasterpassConfigRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class MasterpassRequestBody{
    private String consumerKey;
    private String checkoutId;

    public String getCheckoutId() {
        return checkoutId;
    }

    public void setCheckoutId(String checkoutId) {
        this.checkoutId = checkoutId;
    }

    public String getConsumerKey() {
        return consumerKey;
    }

    public void setConsumerKey(String consumerKey) {
        this.consumerKey = consumerKey;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    private String password;
} 
@RestController
public class PaymentSettingsRestController {
    @Autowired
    private MasterpassConfigRepository masterRepo;
    @Autowired
    private IyzicoConfigRepository iyzicoRepo;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    private PrivateKey getPrivateKey(MultipartFile file, String password) {
        PrivateKey privateKey = null;
        try {
            KeyStore ks = KeyStore.getInstance("PKCS12");
            InputStream inputstream = file.getInputStream();

            ks.load(inputstream, password.toCharArray());
            Enumeration<String> enumeration = ks.aliases();
            String keyAlias = enumeration.nextElement();
            privateKey = (PrivateKey) ks.getKey(keyAlias, password.toCharArray());
        } catch (Exception e) {
            logger.error("error occured at getPrivateKey: "+e.toString());
        }
        return privateKey;
    }
    @PostMapping(path="/setMasterpass")
    public void setMasterpass(@RequestParam("privateKeyFile") MultipartFile file,MasterpassRequestBody body){
        try{
            PrivateKey privateKey = getPrivateKey(file, body.getPassword());
            MasterpassModel model= new MasterpassModel(body.getConsumerKey(),privateKey,body.getCheckoutId());
            masterRepo.save(model);
            
        }
        catch(Exception e){
            System.out.println("err:"+e.toString());
            throw new PasswordException(e);
        }
        
        
    }
    @PostMapping(path="/setIyzico")
    public void setIyzico(IyzicoModel model){
        try{
            iyzicoRepo.save(model);
        }
        catch(Exception e){
            //throw new ResponseStatusException("error occured while creating iyzico config", e);
        }
        
    }
}
