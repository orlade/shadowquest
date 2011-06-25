package Mapgen;

public class GenParams
{
	public int r1_cutoff;
	public int r2_cutoff;
 	public int reps;
 	
 	public GenParams(int r1, int r2, int reps)
 	{
 		this.r1_cutoff = r1;
		this.r2_cutoff = r2;
		this.reps = reps;
	}
}
