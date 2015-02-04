package glueps.adaptors.vle.moodle.utils;

public abstract class AbstractCloning {
   /* public AbstractCloning clone(){

        return (AbstractCloning) ClonaXReflexion.clona(this);

    }*/

 

    public Object javaClone() throws CloneNotSupportedException{

        return super.clone();

    }
}
