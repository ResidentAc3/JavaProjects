import java.io.*;
import java.util.*;

public class main {
	static String[] secpol = {"MaximumPasswordAge = 42", "NewAdministratorName = \"Administrator\""};
	static ArrayList<String> secPoints = new ArrayList<String>();
	public static void main(String[] args) throws Exception
	{
		ArrayList<Boolean> recieve = new ArrayList<Boolean>();
		String[] labels = {"DefaultAccount has been removed",
				"Billy has been removed"};
		readSecpol(secpol);
		String[] users = { "DefaultAccount", "Billy" };
		cleanUp(execCmd("schtasks /query"));
		for(int j = 0; j < users.length;j++)
			recieve.add(userAnalysis(cleanUsers(execCmd("net user")), users, j));
		findPoints(recieve, labels);
	}
	public static ArrayList<String> execCmd(String f) throws Exception
	{
		ArrayList<String> output = new ArrayList<String>();
		ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", f);
		builder.redirectErrorStream(true);
        Process p = builder.start();
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while (true) 
        {
        	line = r.readLine();
        	if (line == null) { break; }
        	output.add(line);
        }
        output.remove(0);
        return output;
	}
	public static String splitter(String base, ArrayList<String> out)
	{
		if(out.contains("ERROR: The system was unable to find the specified registry key or value.")) //This statement merely exists as a fail safe
			return "The registry key does not exist";
		for(String x:out)
			if(!base.contains(x))
				return x;
		return null;
	}
	public static Boolean analysis(String last, String[] ans, int b)
	{
		if(last.contains(ans[b]))
			return true;
		return false;
	}
	public static ArrayList<String> cleanUp(ArrayList<String> mod)
	{
		ArrayList<String> result = new ArrayList<String>();
		for(String x:mod)
			if(!(x.indexOf("Folder")>=0 || x.indexOf("==")>=0 || x.indexOf("TaskName")>=0 || x.indexOf("INFO")>=0 || !x.contains(" ")))
				result.add(x);
		return result;
	}
	public static ArrayList<String> cleanUsers(ArrayList<String> original)
	{
		ArrayList<String> finalUsers = new ArrayList<String>();
		String[] temp;
		for(String x:original)
		{
			if(!(x.indexOf("-")>=0 || x.indexOf("completed")>=0 || x.indexOf("User accounts for") >= 0 || !x.contains(" ")))
			{
				temp = x.split("          ");
				for(String y:temp)
					finalUsers.add(y.trim());
			}
		}
		finalUsers.remove(finalUsers.size() - 1);
		return finalUsers;
	}
	public static Boolean userAnalysis(ArrayList<String> users, String[] arr, int a)
	{
		if(users.contains(arr[a]))
			return false;
		return true;
	}
	public static void findPoints(ArrayList<Boolean> x, String[] as) throws IOException
	{
		ArrayList<String> labelAns = new ArrayList<String>();
		for(int i = 0;i<as.length;i++)
			if(x.get(i))
				labelAns.add(as[i]);
		labelAns.addAll(secPoints);
		writePoints(labelAns);
	}
	public static void writePoints(ArrayList<String> z) throws IOException
	{
		FileWriter points = new FileWriter("C:\\Users\\Rameez\\points.txt");
		for(String point:z)
			points.write(point + " | ");
		points.close();
	}
	public static void readSecpol(String[] policies) throws Exception
	{
		try {
			execCmd("secedit /export /cfg C:\\Users\\Rameez\\new.ini");
		} catch (Exception e) {
			e.printStackTrace();
		}
		ArrayList<String> policy = new ArrayList<String>();
		BufferedReader secpol = new BufferedReader(new FileReader("C:\\Users\\Rameez\\testme.txt"));
		String x = secpol.readLine();
		while(x != null)
		{
			policy.add(x);
			x = secpol.readLine();
		}
		secpol.close();
		secpolPoints(removeBrackets(policy));
	}
	public static boolean ranges(int x, int y, int z)
	{
		return ((y>=x) && (x<=z));
	}
	public static boolean compare(int x, int y)
	{
		return x == y;
	}
	public static ArrayList<String> removeBrackets(ArrayList<String> cleanCrew)
	{
		ArrayList<String> returnList = new ArrayList<String>();
		for(int i = 0; i<cleanCrew.size(); i++)
			if(!cleanCrew.get(i).contains("["))
				returnList.add(cleanCrew.get(i));
		return returnList;
	}
	public static void secpolPoints(ArrayList<String> things)
	{
		for(int i = 0; i < secpol.length; i++)
			for(int j = 0; j < things.size(); j++)
				if(things.get(j).contains(secpol[i]))
					secPoints.add(secpol[i]);
	}
	public static int thatVal(String x)
	{
		return (int)x.charAt(x.length()-2);
	}
}	

	
