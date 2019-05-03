
import org.junit.Assert;
import org.junit.Test;


public class MyBankAppTest {

    @Test
    public void testSuccessCase_ACC334455_1() {
        // Ac Id exist in the transactions
        String[] inputArgs = {"ACC334455", "20/10/2018 12:00:00", "20/10/2018 19:00:00"};
        MyBankApp.Result result = MyBankApp.analyzeData(inputArgs);

        Assert.assertEquals(-25.0, result.amount, 0);
        Assert.assertEquals(1, result.txCount, 0);

    }

    @Test
    public void testSuccessCase_ACC334455_2() {
        // Ac Id exist in the transactions
        String[] inputArgs = {"ACC334455", "20/10/2018 12:00:00", "21/10/2018 19:00:00"};
        MyBankApp.Result result = MyBankApp.analyzeData(inputArgs);

        Assert.assertEquals(-32.25, result.amount, 0);
        Assert.assertEquals(2, result.txCount, 0);

    }

    @Test
    public void testSuccessCase_ACC334455_3() {
        // Ac Id exist in the transactions
        String[] inputArgs = {"ACC334455", "20/10/2018 12:00:00", "25/10/2018 19:00:00"};
        MyBankApp.Result result = MyBankApp.analyzeData(inputArgs);

        Assert.assertEquals(-22.25, result.amount, 0);
        Assert.assertEquals(3, result.txCount, 0);

    }

    @Test
    public void testFailureCase_ACC33445() {
        // Ac Id does not exist in the transactions
        String[] inputArgs = {"ACC33445", "20/10/2018 12:00:00", "20/10/2018 19:00:00"};
        MyBankApp.Result result = MyBankApp.analyzeData(inputArgs);

        Assert.assertEquals(0.0, result.amount, 0);
        Assert.assertEquals(0, result.txCount, 0);

    }

    @Test
    public void testSuccessCase_ACC9988775() {
        // Ac Id exist in the transactions
        String[] inputArgs = {"ACC998877", "20/10/2018 12:00:00", "20/10/2018 19:00:00"};
        MyBankApp.Result result = MyBankApp.analyzeData(inputArgs);

        Assert.assertEquals(-5.0, result.amount, 0);
        Assert.assertEquals(1, result.txCount, 0);

    }

}
