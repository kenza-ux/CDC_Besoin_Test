public class FakeBadge implements IBadge{
    private boolean bloque=false;

    public FakeBadge(){
    }

    @Override
    public boolean estBloque() {
        return this.bloque;
    }

    public void setBloque(boolean bloque) {
        this.bloque = bloque;
    }


}
