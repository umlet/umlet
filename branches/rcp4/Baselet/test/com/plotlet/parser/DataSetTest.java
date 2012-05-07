 package com.plotlet.parser;


public class DataSetTest {
//	
//	private Parser parser;
//	
//	@Before
//	public void initParser()
//	{
//		parser = new Parser();
//	}
//	
//	@Test
//	public void plainDataTest()
//	{
//		String input;
//		
//		input = "data\n"
//			+ "1\t2\t3\n"
//			+ "\n"
//			+ "plot";
//		
//		ParserResult result = parser.parse(input);
//		DataSet dataSet = result.getPlotStateList().get(0).getDataSet();
//		
//		assertEquals(1, dataSet.rows());
//		assertEquals(3, dataSet.cols());
//		
//		assertNull(dataSet.titleCol());
//		assertNull(dataSet.titleRow());
//		
//		assertArrayEquals(new Double[]{1.0,2.0,3.0}, dataSet.row(0));
//		
//		assertArrayEquals(new Double[]{1.0}, dataSet.col(0));
//		assertArrayEquals(new Double[]{2.0}, dataSet.col(1));
//		assertArrayEquals(new Double[]{3.0}, dataSet.col(2));
//	}
//	
//	@Test
//	public void plainDataTestWithTitleRow()
//	{
//		String input;
//		
//		input = "data\n"
//			+ "A\tB\tC\n"
//			+ "1\t2\t3\n"
//			+ "\n"
//			+ "plot";
//		
//		ParserResult result = parser.parse(input);
//		DataSet dataSet = result.getPlotStateList().get(0).getDataSet();
//		
//		assertEquals(1, dataSet.rows());
//		assertEquals(3, dataSet.cols());
//		
//		assertArrayEquals(new String[]{"A","B","C"}, dataSet.titleRow());
//		assertNull(dataSet.titleCol());
//		
//		assertArrayEquals(new Double[]{1.0,2.0,3.0}, dataSet.row(0));
//		
//		assertArrayEquals(new Double[]{1.0}, dataSet.col(0));
//		assertArrayEquals(new Double[]{2.0}, dataSet.col(1));
//		assertArrayEquals(new Double[]{3.0}, dataSet.col(2));
//	}	
//	
//	@Test
//	public void plainDataTestWithTitleColumn()
//	{
//		String input;
//		
//		input = "data\n"
//			+ "A\t1\t2\t3\n"
//			+ "\n"
//			+ "plot";
//		
//		ParserResult result = parser.parse(input);
//		DataSet dataSet = result.getPlotStateList().get(0).getDataSet();
//		
//		assertEquals(1, dataSet.rows());
//		assertEquals(3, dataSet.cols());
//		
//		assertArrayEquals(new String[]{"A"}, dataSet.titleCol());
//		assertNull(dataSet.titleRow());
//		
//		assertArrayEquals(new Double[]{1.0,2.0,3.0}, dataSet.row(0));
//		
//		assertArrayEquals(new Double[]{1.0}, dataSet.col(0));
//		assertArrayEquals(new Double[]{2.0}, dataSet.col(1));
//		assertArrayEquals(new Double[]{3.0}, dataSet.col(2));
//	}		
//	
//	@Test
//	public void plainDataTestWithTitleRowAndColumn()
//	{
//		String input;
//		
//		input = "data\n"
//			+ "X\tA\tB\tC\n"
//			+ "Z\t1\t2\t3\n"
//			+ "\n"
//			+ "plot";
//		
//		ParserResult result = parser.parse(input);
//		DataSet dataSet = result.getPlotStateList().get(0).getDataSet();
//				
//		assertEquals(1, dataSet.rows());
//		assertEquals(3, dataSet.cols());		
//		
//		assertArrayEquals(new String[]{"A","B","C"}, dataSet.titleRow());
//		assertArrayEquals(new String[]{"Z"}, dataSet.titleCol());
//		
//		assertArrayEquals(new Double[]{1.0,2.0,3.0}, dataSet.row(0));
//		
//		assertArrayEquals(new Double[]{1.0}, dataSet.col(0));
//		assertArrayEquals(new Double[]{2.0}, dataSet.col(1));
//		assertArrayEquals(new Double[]{3.0}, dataSet.col(2));
//	}
//	
//	@Test
//	public void edgeDataTestWithTitleRow()
//	{
//		String input;
//		
//		input = "data\n"
//			+ "A\n"
//			+ "1\n"
//			+ "\n"
//			+ "plot";
//		
//		ParserResult result = parser.parse(input);
//		DataSet dataSet = result.getPlotStateList().get(0).getDataSet();
//		
//		assertEquals(1, dataSet.rows());
//		assertEquals(1, dataSet.cols());
//		
//		assertArrayEquals(new String[]{"A"}, dataSet.titleRow());
//		assertNull(dataSet.titleCol());
//		
//		assertArrayEquals(new Double[]{1.0}, dataSet.row(0));	
//		assertArrayEquals(new Double[]{1.0}, dataSet.col(0));
//	}
//	
//	@Test
//	public void edgeDataTestWithTitleColumn()
//	{
//		String input;
//		
//		input = "data\n"
//			+ "A\t1\n"
//			+ "\n"
//			+ "plot";
//		
//		ParserResult result = parser.parse(input);
//		DataSet dataSet = result.getPlotStateList().get(0).getDataSet();
//		
//		assertEquals(1, dataSet.rows());
//		assertEquals(1, dataSet.cols());
//		
//		assertArrayEquals(new String[]{"A"}, dataSet.titleCol());
//		assertNull(dataSet.titleRow());
//		
//		assertArrayEquals(new Double[]{1.0}, dataSet.row(0));	
//		assertArrayEquals(new Double[]{1.0}, dataSet.col(0));
//	}	
//
//	@Test
//	public void edgeDataTestWithTitleRowAndColumn()
//	{
//		String input;
//		
//		input = "data\n"
//			+ "X\tA\n"
//			+ "Z\t1\n"			
//			+ "\n"
//			+ "plot";
//		
//		ParserResult result = parser.parse(input);
//		DataSet dataSet = result.getPlotStateList().get(0).getDataSet();
//		
//		assertEquals(1, dataSet.rows());
//		assertEquals(1, dataSet.cols());
//		
//		assertArrayEquals(new String[]{"A"}, dataSet.titleRow());
//		assertArrayEquals(new String[]{"Z"}, dataSet.titleCol());
//		
//		assertArrayEquals(new Double[]{1.0}, dataSet.row(0));	
//		assertArrayEquals(new Double[]{1.0}, dataSet.col(0));
//	}	
//	

}
