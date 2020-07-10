package com.bod.securefactorbod.fud.services;

import com.bod.securefactorbod.fud.daos.ICustomerDao;
//import com.bod.securefactorbod.fud.daos.IFingerprintDao;
import com.bod.securefactorbod.fud.daos.IFingerprintconfDao;
import com.bod.securefactorbod.fud.daos.IMachineDao;
import com.bod.securefactorbod.fud.models.Customer;
import com.bod.securefactorbod.fud.models.Customermachine;

import com.bod.securefactorbod.fud.models.Fingerprint;
import com.bod.securefactorbod.fud.models.Fingerprintconf;
import com.bod.securefactorbod.fud.models.Response;
import com.bod.securefactorbod.fud.utils.Constants;
import com.bod.securefactorbod.fud.utils.Utils;
import static com.bod.securefactorbod.fud.utils.Utils.sha1;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import static java.util.Collections.list;
import java.util.Date;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class IEufServiceImpl implements IEufService {

    Response result;

    Customermachine machine;

    Customer customer;

//    @Autowired
//    IFingerprintDao ifingerprintdao;
    @Autowired
    IMachineDao imachinedao;

    @Autowired
    ICustomerDao icustomerdao;

    @Autowired
    IFingerprintconfDao ifingerprintconfdao;

    HttpServletRequest request;

    private final IMachineDao imachinefindbyfinger;

    private final IMachineDao imachinefindbyfingeranduser;

    private final IMachineDao imachinefindbyuser;

//    private final IFingerprintDao ifingerprintfindbyidmachine;
    IEufServiceImpl(IMachineDao imachinefindbyfinger, IMachineDao imachinefindbyuser, IMachineDao imachinefindbyfingeranduser) {
        this.imachinefindbyfinger = imachinefindbyfinger;
        this.imachinefindbyuser = imachinefindbyuser;
//        this.ifingerprintfindbyidmachine = ifingerprintfindbyidmachine;
        this.imachinefindbyfingeranduser = imachinefindbyfingeranduser;

    }

    @Override
    public Response validate(Fingerprint finger) {
        // TODO Auto-generated method stub
        result = new Response();
        Customermachine machine = new Customermachine();
        String stringfinger;
        try {

            if (finger != null) {
                if (validateClient(finger.getId_Cuscun_User()).getResponseCode() == Constants.NOTVALIDCLIENT_CODE) {
                    result.setResponseCode(Constants.NOTVALIDCLIENT_CODE);
                    result.setResponseDescription(Constants.NOTVALIDCLIENT_DESC);
                } else {
                    stringfinger = Utils.getHashFinger(finger);
                    //machine = this.findMachineByFinger(stringfinger);
                    machine = this.findMachineByFingerAndUser(stringfinger, finger.getId_Cuscun_User());

                    if (machine != null) {
                        //  if (machine.getCuscunuser().equals(finger.getId_Cuscun_User())) {
                        result.setResponseCode(Constants.FINGERPRINTOK_CODE);
                        result.setResponseDescription(Constants.FINGERPRINTOK_DESC);
                        // } else {
                        // result.setResponseCode(Constants.BADUSER_CODE);
                        // result.setResponseDescription(Constants.BADUSER_DESC);
                        // }
                    } else {
                        result = this.getPercentege(finger);
//                      result.setResponseCode(this.getPercentege(finger).getResponseCode());
//                      result.setResponseDescription(this.getPercentege(finger).getResponseDescription());

                    }

                }
            }

//            } else {
//
//                result.setResponseCode(102);
//                result.setResponseDescription("VALORES NULOS");
//            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    @Override
    public Response enrollMachine(Fingerprint finger, HttpServletRequest req) {
        try {
            // TODO Auto-generated method stub
            result = new Response();
            machine = new Customermachine();
            String hashstring;
            hashstring = Utils.getHashFinger(finger);

            machine = this.findMachineByFingerAndUser(hashstring, finger.getId_Cuscun_User());

            if (machine == null) {
                Customermachine mach = new Customermachine();
                mach.setMach_Fingerprint(hashstring);
                mach.setMach_Start_Date(new Date());
                mach.setMach_Status("1");
                mach.setMach_Last_Connect_Date(new Date());
                if (req.getHeader("User-Agent").indexOf("Mobile") != -1) {
                    mach.setMach_Type("Mobile");
                } else {
                    mach.setMach_Type("PC");
                }
                mach.setCuscunuser(finger.getId_Cuscun_User());
                result = this.saveMachine(mach);
                //Customermachine machinesaved = this.findMachineByFinger(hashstring);
                //finger.setId_Machine(machinesaved.getId_Cus_Machine());
                //SALVA TODOS LOS PARAMETROS DE LA HUELLA DEL EQUIPO
                //ifingerprintdao.save(finger);
                // result.setResponseCode(Constants.MACHINESAVED_CODE);
                // result.setResponseDescription(Constants.MACHINESAVED_DESC);

            } else {
                result.setResponseCode(Constants.FINGERPRINTOK_CODE);
                result.setResponseDescription(Constants.FINGERPRINTOK_DESC);

            }

        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(IEufServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public List<Customermachine> listEnrolledMachines(String usersharedkey) {
        // TODO Auto-generated method stub

        return imachinedao.findMachineBycuscunuser(usersharedkey);

    }

    @Override
    public Response retrieveMachineInfo() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Response validateClient(String shk) {
        // TODO Auto-generated method stub

        Response response = new Response();

        customer = new Customer();

        customer = this.findCustomer(shk);

        if (customer == null) {
            response.setResponseCode(Constants.NOTVALIDCLIENT_CODE);
            response.setResponseDescription(Constants.NOTVALIDCLIENT_DESC);
        } else {
            response.setResponseCode(Constants.VALIDCLIENT_CODE);
            response.setResponseDescription(Constants.VALIDCLIENT_DESC);
        }
        //System.out.println(response.getResponseCode());
        return response;
    }

    @Override
    public Customer findCustomer(String shk) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

        return icustomerdao.findById(shk).orElse(null);

    }

    @Override
    public Customermachine findMachineByFinger(String Fingerprint) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        Customermachine machine = new Customermachine();
        machine = imachinefindbyfinger.findMachineByFingerprint(Fingerprint);

        return machine;

    }

    @Override
    public Response saveMachine(Customermachine machine) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        Response resp = new Response();
        imachinedao.save(machine);
        Customermachine mach = imachinedao.findMachineByFingerprintAndCuscunuser(machine.getFingerprint(), machine.getCuscunuser());
        resp.setCustomermachine(mach);
        resp.setResponseCode(Constants.MACHINESAVED_CODE);
        resp.setResponseDescription(Constants.MACHINESAVED_DESC);

        return resp;

    }

    @Override
    public Response getPercentege(Fingerprint finger) {

        Response resp = new Response();
        try {
            // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            int porcentajetotal = 0;

            String hashmachine = Utils.getHashFinger(finger);

            List<Customermachine> machine = new ArrayList<Customermachine>();

            Fingerprint fingermachine = new Fingerprint();

//            machine = this.findMachineByFingerAndUser(hashmachine,finger.getId_Cuscun_User());
            machine = this.findMachineByUser(finger.getId_Cuscun_User());

            fingermachine = Utils.recoverHashMapFingerprint(machine.get(0).getFingerprint());

            String NameKeyHash = Utils.calulate_ValueHash_NameHash("NamekeyHash", finger.getUsername() + finger.getAzcriptor_Id());
            String ValueKeyHash = Utils.calulate_ValueHash_NameHash("ValueKeyHash", finger.getUsername() + finger.getNum_Comp_Nav() + finger.getAgent_Us_Nav());

            Fingerprintconf parameters = this.listFingerConfParameters();

            if (machine != null && fingermachine != null) {

                if (fingermachine.getNav_On_Line().equals(sha1(finger.getNav_On_Line()))) {
                    porcentajetotal = porcentajetotal + Integer.valueOf(parameters.getNav_On_Line());
                }

                if (fingermachine.getCookies_On().equals(sha1(finger.getCookies_On()))) {
                    porcentajetotal = porcentajetotal + Integer.valueOf(parameters.getCookies_On());
                }

                if (fingermachine.getNum_Ver_Prov().equals(sha1(finger.getNum_Ver_Prov()))) {
                    porcentajetotal = porcentajetotal + Integer.valueOf(parameters.getNum_Ver_Prov());
                }

                if (fingermachine.getName_Prov_Nav().equals(sha1(finger.getName_Prov_Nav()))) {
                    porcentajetotal = porcentajetotal + Integer.valueOf(parameters.getName_Prov_Nav());
                }

                if (fingermachine.getNum_Comp_Nav().equals(sha1(finger.getNum_Comp_Nav()))) {
                    porcentajetotal = porcentajetotal + Integer.valueOf(parameters.getNum_Comp_Nav());
                }

                if (fingermachine.getDen_Nav().equals(sha1(finger.getDen_Nav()))) {
                    porcentajetotal = porcentajetotal + Integer.valueOf(parameters.getDen_Nav());
                }

                if (fingermachine.getPlatform().equals(sha1(finger.getPlatform()))) {
                    porcentajetotal = porcentajetotal + Integer.valueOf(parameters.getPlatform());
                }

                if (fingermachine.getAgent_Us_Nav().equals(sha1(finger.getAgent_Us_Nav()))) {
                    porcentajetotal = porcentajetotal + Integer.valueOf(parameters.getAgent_Us_Nav());
                }

                if (fingermachine.getLang().equals(sha1(finger.getLang()))) {
                    porcentajetotal = porcentajetotal + Integer.valueOf(parameters.getLang());
                }

                if (fingermachine.getVersion_Nav().equals(sha1(finger.getVersion_Nav()))) {
                    porcentajetotal = porcentajetotal + Integer.valueOf(parameters.getVersion_Nav());
                }

                if (fingermachine.getNav_Name().equals(sha1(finger.getNav_Name()))) {
                    porcentajetotal = porcentajetotal + Integer.valueOf(parameters.getNav_Name());
                }

                if (fingermachine.getNo_Reg_User_Action().equals(sha1(finger.getNo_Reg_User_Action()))) {
                    porcentajetotal = porcentajetotal + Integer.valueOf(parameters.getNo_Reg_User_Action());
                }

                if (fingermachine.getJava_On().equals(sha1(finger.getJava_On()))) {
                    porcentajetotal = porcentajetotal + Integer.valueOf(parameters.getJava_On());
                }

                if (fingermachine.getIp_Addr().equals(sha1(finger.getIp_Addr()))) {
                    porcentajetotal = porcentajetotal + Integer.valueOf(parameters.getIp_Addr());
                }

                if (fingermachine.getMac_Addr().equals(sha1(finger.getMac_Addr()))) {
                    porcentajetotal = porcentajetotal + Integer.valueOf(parameters.getMac_Addr());
                }

                if (fingermachine.getVal_Key_Hash().equals(ValueKeyHash)) {
                    porcentajetotal = porcentajetotal + Integer.valueOf(parameters.getVal_Key_Hash());
                }

                if (fingermachine.getName_Key_Hash().equals(NameKeyHash)) {
                    porcentajetotal = porcentajetotal + Integer.valueOf(parameters.getName_Key_Hash());

                }

                if (fingermachine.getCod_Country().equals(sha1(finger.getCod_Country()))) {
                    porcentajetotal = porcentajetotal + Integer.valueOf(parameters.getCod_Country());
                }

                if (porcentajetotal >= 70) {
                    resp.setResponseCode(Constants.FINGERPRINTOK_CODE);
                    resp.setResponseDescription(Constants.FINGERPRINTOK_DESC);
                } else {
                    resp.setResponseCode(Constants.FINGERPRINTBAD_CODE);
                    resp.setResponseDescription(Constants.FINGERPRINTBAD_DESC);
                }
                resp.setCustomermachine(machine.get(0));
            }

        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(IEufServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resp;
    }

//    @Override
//    public List<Fingerprintconf> listFingerConfParameters() {
//        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//        ArrayList<Fingerprintconf> listaparametros = new ArrayList<>();
//        return ifingerprintconfdao.findAll();
//    }
    @Override
    public List<Customermachine> findMachineByUser(String cuscunuser) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return imachinefindbyuser.findMachineBycuscunuser(cuscunuser);
    }

    @Override
    public Customermachine findMachineByFingerAndUser(String finger, String cuscunuser) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return imachinefindbyfingeranduser.findMachineByFingerprintAndCuscunuser(finger, cuscunuser);
    }

    @Override
    public Response saveFingerConfValues(Fingerprintconf fingerprintconfigurations) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

        Response resp = new Response();

//        Fingerprintconf fingerprintconf = new Fingerprintconf();
        ifingerprintconfdao.save(fingerprintconfigurations);

        resp.setResponseCode(Constants.CONFFINGERSAFE_CODE);
        resp.setResponseDescription(Constants.CONFFINGERSAFE_DESC);

        return resp;
    }

    @Override
    public Fingerprintconf listFingerConfParameters() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return ifingerprintconfdao.findById(1).orElse(null);

    }

}
