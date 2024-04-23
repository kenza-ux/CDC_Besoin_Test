package Batiment.Utils;

import Batiment.IPorte;

public class PorteDummy implements IPorte {

    public PorteDummy() throws Exception {
        throw new Exception("cette porte est défaillante");
    }
    @Override
    public boolean ouvrir() {
        //throw new Exception();
        return false;
    }
}
