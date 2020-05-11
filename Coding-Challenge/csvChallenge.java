    import java.sql.Connection;
    import java.sql.Statement;
    import java.sql.PreparedStatement;
    import java.sql.DatabaseMetaData;  
    import java.sql.DriverManager;  
    import java.sql.SQLException; 
    import java.sql.ResultSet;
    import java.io.*;
       
    public class csvChallenge {  
      
        public static void createNewDatabase(String fileName) throws SQLException {  
       
            String url = "jdbc:sqlite:C:/sqlite/db/" + fileName+".db";  
              
            try( Connection conn = DriverManager.getConnection(url)) {  
            
                if (conn != null) {  
                    DatabaseMetaData meta = conn.getMetaData();  
                    System.out.println("The driver name is " + meta.getDriverName());  
                    System.out.println("Created new database: "+fileName);  
                }  
       
            } catch (SQLException e) {  
                System.out.println(e.getMessage());  
            }
        }  
        
        public static void createNewTable(String fileName) throws SQLException {  
            // SQLite connection string  
            String url = "jdbc:sqlite:C://sqlite/db/"+fileName+".db";  
              
            // SQL statement for creating a new table  
            String sql = "CREATE TABLE IF NOT EXISTS GoodData ("  
                    + " A text,\n"  
                    + " B text,\n"  
                    + " C text,\n"
                    + " D text,\n"
                    + " E text,\n"
                    + " F text,\n"
                    + " G text,\n"                    
                    + " H text,\n"
                    + " I text,\n"
                    + " J text\n"                    
                    + ");";   
                      
                    
                      
              
            try (Connection conn = DriverManager.getConnection(url);
                Statement stmt = conn.createStatement()){  

                stmt.execute(sql);  
            } catch (SQLException e) 
            {  
                System.out.println(e.getMessage()); 
                
            }  
        }   
        
        public static void csvProcess(String fileName,long ID)throws IOException{
            File fname = new File("C://sqlite/"+fileName+".csv");
            BufferedReader csvReader = new BufferedReader(new FileReader(fname));
            String  row;
            int total=0,good=0,bad=0;
                while ((row = csvReader.readLine()) != null) {
                    String[] data = row.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)",-1);
                    
                    //divide the substrings per line on the comma as many times as possible and keep trailig empty string
                    for(int i=0;i<data.length;i++){

                        //put the data into the bad information csv file, as soon as you see an empty string you can regard it as bad entry
                        if(data[i].length()==0||data.length>10){
                            csvWriter(fileName+"-"+ID, data);
                            total++;
                            bad++;
                            break;
                        }else if(i==data.length-1){
                            if(data[0].equals("A")&&data[1].equals("B")&&data[2].equals("C")&&data[3].equals("D")&&data[4].equals("E")&&data[5].equals("F")
                            &&data[6].equals("G")&&data[7].equals("H")&&data[8].equals("I")&&data[9].equals("J"))
                                    {
                                     // skip the header

                                    }else{
                                        try{
                                            total++;
                                            good++;
                                            databaseWriter(fileName+"-"+ID, data);
                                        }catch (SQLException e){
                                            System.out.println(e.getMessage()); 
                                        }
                                        
                                    }
                            
                        }
                    }
                }
                logWriter(fileName+"-"+ID,total,good,bad);
                csvReader.close();
        }
        
            public static void databaseWriter(String fileName, String[]data) throws SQLException{
                String url = "jdbc:sqlite:C:/sqlite/db/" + fileName+".db";  
                String sql = " INSERT INTO GoodData(A,B,C,D,E,F,G,H,I,J) VALUES(?,?,?,?,?,?,?,?,?,?)";
            Connection conn = DriverManager.getConnection(url);  
                try {  
                    
                    if (conn != null) {  
                            PreparedStatement ps = conn.prepareStatement(sql);
                            for(int i=0;i<data.length;i++){
                                
                                ps.setString(i+1,data[i]);

                            }
                            ps.executeUpdate();
                            ps.close();
                            
                    }  
           
                } catch (SQLException e) {  
                    System.out.println(e.getMessage());  
                }  finally {
                    
                    try {
                        if (conn != null){
                            conn.close();
                        }
                    } catch (SQLException e) {  
                    System.out.println(e.getMessage());  
                 }
                
            }
        }
        
            public static void selectAll(String fileName)throws SQLException{
            String sql = "SELECT A,B,C,D,E,F,G,H,I,J FROM GoodData";
            String url = "jdbc:sqlite:C:/sqlite/db/" + fileName+".db"; 
          
            try(Connection conn=DriverManager.getConnection(url);
                Statement stmt  = conn.createStatement();
                ResultSet rs  = stmt.executeQuery(sql)) {

                
                // loop through the result set
                while (rs.next()) {
                    System.out.println(rs.getString("A") + "\t" + 
                                       rs.getString("B") + "\t" +
                                       rs.getString("C") + "\t" +
                                       rs.getString("D") + "\t" +
                                       rs.getString("E") + "\t" +
                                       rs.getString("F") + "\t" +
                                       rs.getString("G") + "\t" +
                                       rs.getString("H") + "\t" +
                                       rs.getString("I") + "\t" +
                                       rs.getString("J"));
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        
        
        public static void csvWriter(String fileName,String[] data)throws IOException{
            File fname = new File("C://sqlite/"+fileName+"-bad.csv"); //edited to actually place the csv in the file structure
            BufferedWriter csvWriter = new BufferedWriter(new FileWriter(fname,true));
            for(int i=0;i<data.length;i++){
                if(i==data.length-1){
                csvWriter.append(data[i]);
                csvWriter.append("\n");
                }else{
                csvWriter.append(data[i]);
                csvWriter.append(",");
                }
            }
            csvWriter.flush();
            csvWriter.close();
            
            
            
            
        }
        public static void logWriter(String fileName,int count,int valid, int invalid)throws IOException{
            File fname = new File("C://sqlite/"+fileName+".log"); //edited to actually place the csv in the file structure
            BufferedWriter csvWriter = new BufferedWriter(new FileWriter(fname,true));
            
                
                csvWriter.append("# of Total Records: "+count);
                csvWriter.append("\r\n");
                
                csvWriter.append("# of Good Values: "+valid);
                csvWriter.append("\r\n");
                
                csvWriter.append("# of bad Values: "+invalid);
                csvWriter.append("\r\n"); 
                
            csvWriter.flush();
            csvWriter.close();
            
            
        }
            public static void main(String[] args) throws SQLException {  
                //createNewDatabase("ms3Interview - Jr Challenge 2");
                long tag=System.currentTimeMillis();
                createNewTable("ms3Interview - Jr Challenge 2"+"-"+tag);
                try{
                    csvProcess("ms3Interview - Jr Challenge 2",tag);
                }catch(IOException e){
                    System.out.println(e.getMessage());  
                }
                
                 /*selectAll("ms3Interview - Jr Challenge 2");                
                  * //used to print table output while debugging
                    */
               

            }  
    }  