package villagegaulois;

import java.util.Iterator;

import personnages.Chef;
import personnages.Gaulois;

public class Village {
	private String nom;
	private Chef chef;
	private Gaulois[] villageois;
	private int nbVillageois = 0;
	private Marche marche;

	public Village(String nom, int nbVillageoisMaximum, int nbEtalMarche) {
		this.nom = nom;
		villageois = new Gaulois[nbVillageoisMaximum];
		marche = new Marche(nbEtalMarche);
	}

	public String getNom() {
		return nom;
	}

	public void setChef(Chef chef) {
		this.chef = chef;
	}

	public void ajouterHabitant(Gaulois gaulois) {
		if (nbVillageois < villageois.length) {
			villageois[nbVillageois] = gaulois;
			nbVillageois++;
		}
	}

	public Gaulois trouverHabitant(String nomGaulois) {
		if (nomGaulois.equals(chef.getNom())) {
			return chef;
		}
		for (int i = 0; i < nbVillageois; i++) {
			Gaulois gaulois = villageois[i];
			if (gaulois.getNom().equals(nomGaulois)) {
				return gaulois;
			}
		}
		return null;
	}

	public String afficherVillageois() throws VillageSansChefException {
		if (chef==null) {
			throw new VillageSansChefException("Le village n'a pas de chef");
		}
		StringBuilder chaine = new StringBuilder();
		if (nbVillageois < 1) {
			chaine.append("Il n'y a encore aucun habitant au village du chef " + chef.getNom() + ".\n");
		} else {
			chaine.append("Au village du chef " + chef.getNom() + " vivent les légendaires gaulois :\n");
			for (int i = 0; i < nbVillageois; i++) {
				chaine.append("- " + villageois[i].getNom() + "\n");
			}
		}
		return chaine.toString();
	}

	private static class Marche {
		private Etal etals[];

		private Marche(int nbEtals) {
			this.etals = new Etal[nbEtals];
			for (int i = 0; i < nbEtals; i++) {
				etals[i] = new Etal();
			}
		}

		public void utiliserEtal(int indiceEtal, Gaulois vendeur, String produit, int nbProduit) {
			etals[indiceEtal].occuperEtal(vendeur, produit, nbProduit);
		}

		public int trouverEtalLiber() {
			int indiceEtalLibre = -1;
			for (int i = 0; i < etals.length; i++) {
				Etal etal = etals[i];
				if (etal.isEtalOccupe()) {
				} else {
					indiceEtalLibre = i;
				}
			}
			return indiceEtalLibre;
		}

		public Etal[] trouverEtals(String produit) {
			int nbEtalsProduit = 0;
			for (Etal etal : etals) {
				if (etal.contientProduit(produit)) {
					nbEtalsProduit++;
				}
			}
			Etal[] etalsProduit = new Etal[nbEtalsProduit];
			int indice = 0;
			for (Etal etal : etals) {
				if (etal.contientProduit(produit)) {
					etalsProduit[indice] = etal;
					indice++;
				}
			}
			return etalsProduit;
		}

		public Etal trouverVendeur(Gaulois gaulois) {
			for (Etal etal : etals) {
				if (etal.getVendeur() == gaulois) {
					return etal;
				}
			}
			return null;
		}

		public String afficherMarche() {
			String affichage = null;
			int nbetalVide = 0;
			for (Etal etal : etals) {
				if (etal.isEtalOccupe()) {
					affichage += etal.afficherEtal();
				} else {
					nbetalVide++;
				}
			}
			return affichage + "Il reste " + nbetalVide + " �tals non utilis�s dans le march�.\n";
		}

	}

	public String installerVendeur(Gaulois vendeur, String produit, int nbProduit) {
		StringBuilder affichage = new StringBuilder();
		affichage.append(vendeur.getNom());
		affichage.append(" cherche un endroit pour vendre ");
		affichage.append(nbProduit);
		affichage.append(produit);

		int nbEtal = marche.trouverEtalLiber();
		marche.utiliserEtal(nbEtal, vendeur, produit, nbProduit);

		affichage.append(".\nLe vendeur ");
		affichage.append(vendeur.getNom());
		affichage.append(" vend des ");
		affichage.append(produit);
		affichage.append(" � l'�tal n�");
		affichage.append(nbEtal);

		return affichage.toString();
	}

	public String rechercherVendeursProduit(String produit) {
		StringBuilder vendeurs = new StringBuilder();
		Etal etalVendeur[] = marche.trouverEtals(produit);
		if (etalVendeur.length==0) {
			vendeurs.append("Il n'y a pas de vendeur qui propose "+produit+" au marche.");
		}else if (etalVendeur.length==1) {
			vendeurs.append("Seul le vendeur "+etalVendeur[0].getVendeur().getNom()+" propose des "+produit+" au marche.");
		} else {
			vendeurs.append("Les venduers qui proposent des "+produit+" sont :\n");
			for (Etal etal : etalVendeur) {
				vendeurs.append("- "+etal.getVendeur().getNom()+"\n");
			}
		}
		return vendeurs.toString();
		
	}
	
	public Etal rechercherEtal(Gaulois vendeur) {
		for (Etal etal : marche.etals) {
			if (etal.getVendeur()==vendeur) {
				return etal;
			}
		}
		return null;
	}
	
	public String partirVendeur(Gaulois vendeur) {
		Etal etal = rechercherEtal(vendeur);
		return etal.libererEtal();
	}
	
	public String afficherMarche() {
		StringBuilder affichage = new StringBuilder();
		affichage.append("Le marche du village \""+getNom()+"\" possede plusieurs etals :\n");
		int nbEtalLibre = 0;
		for (Etal etal : marche.etals) {
			if (etal.isEtalOccupe()) {
				affichage.append(etal.afficherEtal());
			} else {
				nbEtalLibre++;
			}
		}
		affichage.append("Il reste "+nbEtalLibre+" etals non utilises dasn le marche\n");
		return affichage.toString();
	}
	
	private static class VillageSansChefException extends RuntimeException {

		public VillageSansChefException() {
			super();
		}

		public VillageSansChefException(String message, Throwable cause) {
			super(message, cause);
		}

		public VillageSansChefException(String message) {
			super(message);
		}

		public VillageSansChefException(Throwable cause) {
			super(cause);
		}
		
	}
	
	

}