package histoire;

import personnages.Gaulois;
import villagegaulois.Etal;

public class ScenarioCasDergrade {

public static void main(String[] args) {
	Etal etal = new Etal();
	Gaulois g = null;
	etal.acheterProduit(1, g);
	System.out.println("Fin du test");
	}
}
