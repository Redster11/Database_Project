import java.sql.*;
import java.util.Calendar;
import java.util.Scanner;
public class Run {
	public static Connection connectToDatabase(String databaseURL) throws SQLException
	{
		Connection connection = DriverManager.getConnection(databaseURL);
		return connection;
	}
	public static void SearchSchedule(Connection connection, String StartLocationName, String DestinationName, String Date) throws SQLException
	{
		String sql = 
				"SELECT T.StartLocationName, T.DestinationName, D.TripDate, D.ScheduledStartTime, D.ScheduledArrivalTime, D.BusID, D.DriverName, T.TripNumber\r\n" + 
				"FROM Trip as T, TripOffering as D\r\n" + 
				"WHERE T.StartLocationName=\""+StartLocationName+"\" AND T.DestinationName=\""+DestinationName+"\" AND D.TripDate=\""+ Date +"\" AND T.TripNumber = D.TripNumber";
		Statement statement = connection.createStatement();
		ResultSet result = statement.executeQuery(sql);
		System.out.println("The Current Trips From: " + StartLocationName +" To: " + DestinationName + " On: " + Date);
		int count = 0;
		while (result.next())
		{
			count++;
			Date dateTest = result.getDate("TripDate");
			Time StartTime = result.getTime("ScheduledStartTime");
			Time ArriveTime = result.getTime("ScheduledArrivalTime");
			String DriverName = result.getString("DriverName");
			int BusID = result.getInt("BusID");
			int TripNumber = result.getInt("TripNumber");
			if(count == 1)
			{
				System.out.println("TripNumber: " + TripNumber + "\nDate of Trip: " + dateTest);
			}
			System.out.println( count +"\n    Start Time: "+ StartTime +"\n    Arrival Time: "+ArriveTime+"\n    Driver: " + DriverName+"\n    BusID: "+ BusID);
			
		}
	}
	public static void  DeleteTrip(Connection connection, int TripNumber, Date correctDate, Time ScheduledStartTime) throws SQLException
	{
		String sql =
				"DELETE FROM TripOffering WHERE TripNumber=\""+TripNumber+"\" AND TripDate=#"+correctDate+"# AND ScheduledStartTime=#" + ScheduledStartTime + "#";
		Statement statement = connection.createStatement();
		int result = statement.executeUpdate(sql);
		if(result == 1)
		{
			System.out.println("The Trip Has Been Deleted");
		}
	}
	public static void AddTripOffering(Connection connection, int TripNumber, Date correctDate, Time ScheduledStartTime, Time ScheduledArrivalTime, String DriverName, int BusID) throws SQLException
	{
		String sql =
				"INSERT INTO TripOffering (TripNumber, TripDate, ScheduledStartTime, ScheduledArrivalTime, DriverName, BusID) VALUES (\""+TripNumber+"\", #"+correctDate+"#, #"+ ScheduledStartTime +"#, #"+ScheduledArrivalTime +"#, \""+DriverName+"\", \""+BusID+"\")";
		Statement statement = connection.createStatement();
		int result = statement.executeUpdate(sql);
		if(result == 1)
		{
			System.out.println("The Trip Has Been Created");
		}
	}
	public static void UpdateTripDriver(Connection connection, int TripNumber, Date TripDate, Time ScheduledStartTime, String DriverName) throws SQLException
	{
		String sql =
				"UPDATE TripOffering Set DriverName =\"" + DriverName+"\" WHERE TripNumber=\""+TripNumber+"\" AND TripDate=#"+TripDate+"# AND ScheduledStartTime=#" + ScheduledStartTime + "#";
		Statement statement = connection.createStatement();
		int result = statement.executeUpdate(sql);
		if(result == 1)
		{
			System.out.println("The Trip Driver Has Been Updated");
		}
	}
	public static void UpdateTripBus(Connection connection, int TripNumber, Date TripDate, Time ScheduledStartTime, int BusID) throws SQLException
	{
		String sql =
				"UPDATE TripOffering Set BusID =\"" + BusID+"\" WHERE TripNumber=\""+TripNumber+"\" AND TripDate=#"+TripDate+"# AND ScheduledStartTime=#" + ScheduledStartTime + "#";
		Statement statement = connection.createStatement();
		int result = statement.executeUpdate(sql);
		if(result == 1)
		{
			System.out.println("The Trip Driver Has Been Updated");
		}
	}
	public static void TripStopInfo(Connection connection, int TripNumber) throws SQLException
	{
		String sql =
				"SELECT T.StartLocationName, T.DestinationName, I.TripNumber, I.StopNumber, I.SequenceNumber, I.DrivingTime\r\n" +
				"FROM TripStopInfo AS I, Trip AS T\r\n"+
				"WHERE I.TripNumber =\""+ TripNumber + "\" AND T.TripNumber=I.TripNumber";
		Statement statement = connection.createStatement();
		ResultSet result = statement.executeQuery(sql);
		int count = 0;
		while (result.next())
		{
			count++;
			String SLocation = result.getString("StartLocationName");
			String Destination = result.getString("DestinationName");
			int TripNumberSQL = result.getInt("TripNumber");
			int StopNumber = result.getInt("StopNumber");
			String SequenceStops = result.getString("SequenceNumber");
			String DriveTime = result.getString("DrivingTime");
			if(count == 1)
				System.out.println("The TripNumber: " + TripNumber + " Goes From: " + SLocation + " To: " + Destination);
			System.out.println("The Number of Stops is: " + StopNumber+"\nThe Sequence of Stops on this trip is: " + SequenceStops + "\nExpected Drive time is: " + DriveTime + "mins\n");
		}
	}
	public static void DriverSchedule(Connection connection, String DriverName, Date TripDate) throws SQLException
	{
		Calendar c = Calendar.getInstance();
		c.setTime(TripDate);
		c.add(Calendar.DATE, 7);
		Date EndTripDate = new Date(c.getTimeInMillis());
		String sql =
				"SELECT T.TripNumber, T.TripDate, T. ScheduledStartTime, T.ScheduledArrivalTime, T.BusID\r\n"
				+ "FROM TripOffering AS T\r\n"
				+ "WHERE T.DriverName=\"" + DriverName + "\" AND T.TripDate BETWEEN #" + TripDate + "# AND #" + EndTripDate + "#";
		Statement statement = connection.createStatement();
		ResultSet result = statement.executeQuery(sql);
		int count = 0;
		while (result.next())
		{
			count++;
			if(count == 1)
			{
				System.out.println("The Schedule for " + DriverName + ":");
			}
			int TripNumber = result.getInt("TripNumber");
			Date TripDateSQL = result.getDate("TripDate");
			Time STime = result.getTime("ScheduledStartTime");
			Time EndTime = result.getTime("ScheduledArrivalTime");
			int BusID = result.getInt("BusID");
			System.out.println("\tTrip Number: " + TripNumber+"\n\tTrip Date: " + TripDateSQL + "\n\tStart Time: "+STime + "\tEnd Time: " + EndTime +"\n\tBusID: " + BusID + "\n");
		}
	}
	public static void addDriver(Connection connection, String DriverName, String Phone) throws SQLException
	{
		String sql =
				"INSERT INTO Driver (DriverName, DriverTelephoneNumber) VALUES (\"" + DriverName + "\", \"" + Phone+"\")";
		Statement statement = connection.createStatement();
		int result = statement.executeUpdate(sql);
		if(result == 1)
		{
			System.out.println("The Driver Has Been Added");
		}
	}
	public static void addBus(Connection connection, int BusID, String Model, String Year) throws SQLException
	{
		String sql =
				"INSERT INTO Bus (BusID, Model, Year) VALUES (\"" + BusID + "\", \"" + Model+ "\", \""+ Year +"\")";
		Statement statement = connection.createStatement();
		int result = statement.executeUpdate(sql);
		if(result == 1)
		{
			System.out.println("The Bus Has Been Added");
		}
	}
	public static void deleteBus(Connection connection, int BusID) throws SQLException
	{
		String sql =
				"DELETE FROM Bus WHERE BusID=\""+BusID+"\"";
		Statement statement = connection.createStatement();
		int result = statement.executeUpdate(sql);
		if(result == 1)
		{
			System.out.println("The Bus Has Been Deleted");
		}
	}
	public static void DriverTripStopInfo(Connection connection, int TripNumber, Date TripDate, Time ScheduledStartTime, int StopNumber, Time ActualStartTime, Time ActualArrivalTime, int NumberOfPassengerIn, int NumberOfPassengerOut) throws SQLException
	{
		String sql1=
				"SELECT ScheduledArrivalTime FROM TripOffering WHERE TripNumber=\"" + TripNumber+"\" AND TripDate=#"+TripDate+"# AND ScheduledStartTime=#" + ScheduledStartTime + "#";
		Statement statement = connection.createStatement();
		ResultSet result = statement.executeQuery(sql1);
		Time ScheduledArrivalTime = null;
		while(result.next())
		{
			ScheduledArrivalTime = result.getTime("ScheduledArrivalTime");
		}
		String sql2=
				"INSERT INTO ActualTripStopInfo (TripNumber, TripDate, ScheduledStartTime, StopNumber, ScheduledArrivalTime, ActualStartTime, ActualArrivalTime, NumberOfPassengerIn, NumberOfPassengerOut) VALUES (\""+TripNumber+"\", #"+ TripDate + "#, #" + ScheduledStartTime + "#, \"" + StopNumber + "\", #" + ScheduledArrivalTime + "#, #" + ActualStartTime + "#, #" + ActualArrivalTime + "#, \"" + NumberOfPassengerIn + "\", \"" + NumberOfPassengerOut + "\")"; 
		int result2 = statement.executeUpdate(sql2);
		if(result2 == 1)
		{
			System.out.println("The Trip Info has been Added");
		}
	}
	public static void main(String[] args) throws SQLException 
	{
		String databaseURL = "jdbc:ucanaccess://D://Eclipse//WorkSpace2//JDBC_Project//Pomona_Transit_System.accdb;showschema=true;sysschema=true";
		Connection connection = connectToDatabase(databaseURL);
		Scanner kb = new Scanner(System.in);
		boolean running = true;
		while(running) 
		{
			System.out.println("Please Enter the value you would like to do:\n0. Exit Program\n1. Search for Schedule\n2. Delete a Trip\n3. Add a Trip\n4. Update Trip Driver\n5. Get TripInfo on Stops\n6. Driver Weekly Schedule"
					+ "\n7. Add a Driver\n8. Add a Bus\n9. Delete a Bus\n10.Insert Record For a Trip Made");
			int userChoice = kb.nextInt();
			kb.nextLine();
			switch(userChoice)
			{
			case 0:
				running = false;
				break;
			case 1:
				System.out.println("Please Enter a StartLocation");
				String StartLocation = kb.nextLine();
				System.out.println("Please Enter a Destination");
				String Destination = kb.nextLine();
				System.out.println("Please Enter a Date(YYYY-MM-DD)");
				String tripDate = kb.nextLine();
				
				
				SearchSchedule(connection, StartLocation, Destination, tripDate);
				break;
			case 2:
				System.out.println("Please Enter a Trip Number");
				int TripNumber = kb.nextInt();
				kb.nextLine();
				System.out.println("Please Enter a Date(YYYY-[M]M-[D]D)");
				tripDate = kb.nextLine();
				Date correctDate = Date.valueOf(tripDate);
				System.out.println("Please Enter the Scheduled Start Time(hh:mm:ss)");
				String startTime = kb.nextLine();
				Time correctTime = Time.valueOf(startTime);
				DeleteTrip(connection, TripNumber, correctDate, correctTime);
				break;
			case 3:
				System.out.println("Please Enter a Trip Number");
				TripNumber = kb.nextInt();
				System.out.println("Please Enter a Date(YYYY-[M]M-[D]D)");
				kb.nextLine();
				tripDate = kb.nextLine();
				correctDate = Date.valueOf(tripDate);
				System.out.println("Please Enter the Scheduled Start Time(hh:mm:ss)");
				startTime = kb.nextLine();
				correctTime = Time.valueOf(startTime);
				System.out.println("Please Enter the Scheduled Arrival Time(hh:mm:ss)");
				String endTime = kb.nextLine();
				Time correctEndTime = Time.valueOf(endTime);
				System.out.println("Please Enter the Driver's Name");
				String DriverName = kb.nextLine();
				System.out.println("Please Enter the BusID");
				int BusID = kb.nextInt();
				kb.nextLine();
				AddTripOffering(connection, TripNumber, correctDate, correctTime, correctEndTime, DriverName, BusID);
				break;
			case 4:
				System.out.println("Please Enter a Trip Number");
				TripNumber = kb.nextInt();
				kb.nextLine();
				System.out.println("Please Enter a Date(YYYY-[M]M-[D]D)");
				tripDate = kb.nextLine();
				correctDate = Date.valueOf(tripDate);
				System.out.println("Please Enter the Scheduled Start Time(hh:mm:ss)");
				startTime = kb.nextLine();
				correctTime = Time.valueOf(startTime);
				System.out.println("Please Enter the new Driver Name");
				DriverName = kb.nextLine();
				UpdateTripDriver(connection, TripNumber, correctDate, correctTime, DriverName);
				break;
			case 5:
				System.out.println("Please Enter a Trip Number");
				TripNumber = kb.nextInt();
				kb.nextLine();
				TripStopInfo(connection, TripNumber);
				break;
			case 6:
				System.out.println("Please Enter a Driver to Look at");
				DriverName = kb.nextLine();
				System.out.println("Please Enter the Date you would like to look from(YYYY-[M]M-[D]D)");
				String DateSearch = kb.nextLine();
				correctDate = Date.valueOf(DateSearch);
				DriverSchedule(connection, DriverName, correctDate);
				break;
			case 7:
				System.out.println("Please Enter the Driver's Name");
				DriverName = kb.nextLine();
				System.out.println("Please Enter the Driver's Phone Number");
				String PhoneNum = kb.nextLine();
				addDriver(connection, DriverName, PhoneNum);
				break;
			case 8:
				System.out.println("Please Enter a BusID Number");
				BusID = kb.nextInt();
				kb.nextLine();
				System.out.println("Enter the Bus Model");
				String Model = kb.nextLine();
				System.out.println("Enter the Year the Bus was made");
				String Year = kb.nextLine();
				addBus(connection, BusID, Model, Year);
				break;
			case 9:
				System.out.println("Please Enter the BusID to Delete");
				BusID = kb.nextInt();
				kb.nextLine();
				deleteBus(connection, BusID);
				break;
			case 10:
				System.out.println("Please Enter a Trip Number");
				TripNumber = kb.nextInt();
				kb.nextLine();
				System.out.println("Please Enter a Date(YYYY-[M]M-[D]D)");
				tripDate = kb.nextLine();
				correctDate = Date.valueOf(tripDate);
				System.out.println("Please Enter the Scheduled Start Time(hh:mm:ss)");
				startTime = kb.nextLine();
				correctTime = Time.valueOf(startTime);
				System.out.println("Enter the Stop Number");
				int StopNumber= kb.nextInt();
				kb.nextLine();
				System.out.println("Please Enter the Start Time that You actually arrived at(hh:mm:ss)");
				String ActualStartTime = kb.nextLine();
				Time ActualSTimeFix = Time.valueOf(ActualStartTime);
				System.out.println("Please Enter the Arrival Time that You actually arrived at the New Location(hh:mm:ss)");
				String ActualArrivalTime = kb.nextLine();
				Time ActualATimeFix = Time.valueOf(ActualArrivalTime);
				System.out.println("Enter the Amount of people that got onto the bus");
				int NumIn= kb.nextInt();
				kb.nextLine();
				System.out.println("Enter the Amount of people that got off the bus");
				int NumOut= kb.nextInt();
				kb.nextLine();
				DriverTripStopInfo(connection, TripNumber, correctDate, correctTime, StopNumber, ActualSTimeFix, ActualATimeFix, NumIn, NumOut);
				break;
			}
		}
	}
}
