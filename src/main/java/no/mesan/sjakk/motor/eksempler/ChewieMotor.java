package no.mesan.sjakk.motor.eksempler;

import java.util.List;

import chesspresso.position.Position;
import no.mesan.sjakk.motor.AbstraktSjakkmotor;
import no.mesan.sjakk.motor.Brikke;
import no.mesan.sjakk.motor.Posisjon;
import no.mesan.sjakk.motor.Trekk;

public class ChewieMotor extends AbstraktSjakkmotor {
	
	private enum Faser {
		SLUTTSPILL, AAPNING, MIDTSPILL
	}

    private int [] startPosition = new int[] {
    	500,300,320,900,100000,320,300,500,
    	100,100,100,100,100,100,100,100,
    	0,0,0,0,0,0,0,0,
    	0,0,0,0,0,0,0,0,
    	0,0,0,0,0,0,0,0,
    	0,0,0,0,0,0,0,0,
    	100,100,100,100,100,100,100,100,
    	500,300,320,900,100000,320,300,500
    };
    
     int sentrumsBonus[] = new int[] { // char is sufficient as well, also unsigned
    		  0, 0, 0, 0, 0, 0, 0, 0,
    		  0, 5, 5, 5, 5, 5, 5, 0,
    		  0, 5, 15, 15, 15, 15, 5, 0,
    		  0, 5, 15, 40, 40, 15, 5, 0,
    		  0, 5, 15, 40, 40, 15, 5, 0,
    		  0, 5, 15, 15, 15, 15, 5, 0,
    		  0, 5, 5, 5, 5, 5, 5, 0,
    		  0, 0, 0, 0, 0, 0, 0, 0
    };
    
    
    private String LINJER = "ABCDEFGH";
    
    private String tilRuteNavn(int ruteNr){
    	int linjeNr = ruteNr % 8;
    	int rad = (ruteNr / 8) + 1;
        return String.valueOf(LINJER.charAt(linjeNr)) + String.valueOf(rad);
    }
    
    private String trekkTilString(Trekk trekk){
    	return tilRuteNavn(trekk.fraRute()) + "->" + tilRuteNavn(trekk.tilRute());
    }

	@Override
	protected void finnBesteTrekk(final Posisjon posisjon) {
		// Safer med et tilfeldit trekk først
		settTilfeldigTrekk(posisjon);
        settSpillFase(posisjon);

        final List<Trekk> alleLovligeTrekk = posisjon.alleTrekk();
        
        //Posisjonsbonus
        int [] posisjoneringsBonus = trekkRangering(alleLovligeTrekk, sentrumsBonus);
        
        //Slagbonus
        
        //Soliditetsbonus
        
        for (int i = 0; i < alleLovligeTrekk.size(); i++){
        	System.out.println(trekkTilString(alleLovligeTrekk.get(i)) + " Posisjonsbonus: " + posisjoneringsBonus[i]);
        }
        
	}
	
	private int[] trekkRangering (List<Trekk> trekkListe, int [] ruteRangeringsTabell){
		int [] rangeringsTabell = new int[trekkListe.size()];
		
		for (int i = 0; i < rangeringsTabell.length; i++){
			int tilRute = trekkListe.get(i).tilRute();
			rangeringsTabell[i] = ruteRangeringsTabell[tilRute];
		}
		
		return rangeringsTabell;
	}
	
	private void settSpillFase(final Posisjon posisjon){

		int sumMateriell = sumMateriell(posisjon);
		System.out.println("Sum of material " + sumMateriell);
		
		System.out.println("Antall brikker beveget" + antallBrikkerSomHarBevegetSeg(posisjon));
		
		if (sumMateriell >= 6000 && antallBrikkerSomHarBevegetSeg(posisjon) < 10) {
			System.out.println("Opening");
		} else if (sumMateriell >= 3500 && antallBrikkerSomHarBevegetSeg(posisjon) >= 10){
			System.out.println("Midgame");
		} else {
			System.out.println("Endgame");
		}
	}



	private int sumMateriell(final Posisjon posisjon){
		int sumMateriell = 0;

		for (int i = 0; i < 64; i++){
			Brikke brikke = posisjon.brikkePaaRute(i);
			int value = Math.abs(brikke.verdi());
			
			//Ikke regn med kongen
			if (value < 1000)
				sumMateriell += value;
		}
		
		return sumMateriell;
	}

	private int antallBrikkerSomHarBevegetSeg(final Posisjon posisjon){
		int antallBrikkerBeveget = 0;
		
		for (int i = 0; i < 64; i++){
			if (posisjon.brikkePaaRute(i).verdi() != startPosition[i])
				antallBrikkerBeveget++;
		}
		
		return antallBrikkerBeveget/2;
	}

	private void finnMotstanderKongenOgOppdaterFeltViktighet() {
		// TODO Auto-generated method stub

	}

	private Trekk finnMestSentraleTrekk() {
		// TODO Auto-generated method stub
		return null;
	}

	private Trekk finnBesteSlagTrekk(){
		return null;
	}



	/**
	 * Setter et tilfeldig trekk blant alle lovlige i posisjonen.
	 * 
	 * @param posisjon
	 */
	private void settTilfeldigTrekk(final Posisjon posisjon) {
		final List<Trekk> alleLovligeTrekk = posisjon.alleTrekk();
		final int index = (int) (alleLovligeTrekk.size() * Math.random());
		settBesteTrekk(alleLovligeTrekk.get(index));
	}

	@Override
	public String lagetAv() {
		return "arnabkd";
	}

	@Override
	public String navn() {
		return "Chewie";
	}

	public static void main(final String[] args) {
		new ChewieMotor().start();
	}
}
