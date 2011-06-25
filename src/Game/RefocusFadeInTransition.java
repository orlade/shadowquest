package Game;

import org.newdawn.slick.Color;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.transition.FadeInTransition;

import World.ExitZone;

/** A bit of a hack class really... I needed some way to splice player/camera repositioning
 * in between the fade out and the fade in, so this class simply calls its superclass to
 * fade out, but throws in a call to the GPS (to position the player/camera) before it starts.
 *  
 * @author Oliver
 *
 */
public class RefocusFadeInTransition extends FadeInTransition
{
	/** The exit zone to jump the player to if going UP */
	private ExitZone targetZone = null;
	
	public RefocusFadeInTransition(Color color)
	{
		super(color);
	}
	
	public RefocusFadeInTransition(Color color, ExitZone z)
	{
		super(color);
		targetZone = z;
		System.out.println("FADEZ = "+z);
	}
	
	@Override
	/** Being the transition by relocating the player to the new entrance and refocusing the camera
	 * 
	 * @param firstState The state being transitioned from
	 * @param secondState The state being transitioned to
	 */
	public void init(GameState firstState, GameState secondState)
	{
		super.init(firstState, secondState);
		
		// Call the method to move the player/camera
		GameplayState fs = (GameplayState)firstState;
		GameplayState ss = (GameplayState)secondState;
		fs.changeLevelRefocus(fs, ss, targetZone);
	}
}
