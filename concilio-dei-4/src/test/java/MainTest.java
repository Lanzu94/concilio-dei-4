

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class MainTest {
	public static void main(String[]arg){
		testActionGame();
		testPrepare();
	}
	public static void testActionGame(){
		Result result=JUnitCore.runClasses(TestActionGame.class);
		for(Failure failure : result.getFailures())
		{
			System.out.println(failure.toString());
		}
		System.out.println(result.wasSuccessful());
	}
	private static void testPrepare(){
		Result result=JUnitCore.runClasses(TestPrepareGame.class);
		for(Failure failure : result.getFailures())
		{
			System.out.println(failure.toString());
		}
		System.out.println(result.wasSuccessful());
	}
}
