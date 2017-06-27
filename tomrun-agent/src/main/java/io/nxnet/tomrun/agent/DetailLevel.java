package io.nxnet.tomrun.agent;

public enum DetailLevel {
	
	OFF(Integer.MIN_VALUE),
	
	SUITE(0),
	
	CASE(1),
	
	TEST(2),
	
	ACTION(3),
	
	ALL(Integer.MAX_VALUE);
	
	private DetailLevel(int level)
	{
		this.level = level;
	}
	
	private int level;

	public int getInt()
	{
		return level;
	}
}
