
import Andromeda.GameObject.GameObject;
import Andromeda.GameObject.MouseCaller;
import Andromeda.GameObject.ObjectManipulator;
import Andromeda.GameObject.BoxObject;
import Andromeda.Manager.GameManager;
import Andromeda.Utils.Vector2;


import java.awt.Color;
import java.lang.reflect.InvocationTargetException;


/**
 * Classname: Main
 * Author: Elias Jätte
 * Date: 18/07/26
 */
public class Main {
    public static void main(String[] args) throws InvocationTargetException, InterruptedException {

        new Main().test();
    }

    public void test(){
        GameManager gameManager = GameManager.getInstance();
        gameManager.startThread();
        gameManager.getScene().addGameObject(new MouseCaller());

        GameObject g1 = new BoxObject();
        g1.transform.setPosition(new Vector2(5,5));
        g1.transform.scale(new Vector2(2,2));
        g1.rigidbody.setMass(60);
        g1.transform.rotate(20);
        g1.rigidbody.isKinematic = false;
        g1.rigidbody.elasticity = 0.1;
        g1.collider.setDefaultColor(new Color(144, 66, 255));
        GameObject.instantiate(g1);

        GameObject g2 = new BoxObject();
        g2.transform.setPosition(new Vector2(5,14));
        g2.transform.scale(new Vector2(2,2));
        g2.rigidbody.setMass(60);
        g2.transform.rotate(35);
        g2.rigidbody.isKinematic = false;
        g2.rigidbody.elasticity = 0.1;
        g2.collider.setDefaultColor(new Color(144, 66, 255));
        GameObject.instantiate(g2);


        ObjectManipulator s1 = new ObjectManipulator();
        s1.transform.setPosition(new Vector2(5.5,0));

        GameObject.instantiate(s1);
    }
}
