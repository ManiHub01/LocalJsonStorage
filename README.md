# LocalJsonStorage

 To store the dataS based on key and Value please follow the bellow steps 

 1) Crate one java project from any of your IDE. 

 2) Add this Fwbacukendtask-0.0.1-SNAPSHOT jar file into your buidPath. 
 
 3) Create one Java file and then copy and past the below snippet into your class.
 
 4) Run it.
 <pre>
      import com.fasterxml.jackson.databind.node.ObjectNode;
      import com.mypack.JsonStorage;
      public class FwStorageTest {
      public static void main(String[] args) {
      /* CREATE Operation*/
      
      String optionalFilePath = "";
      JsonStorage obj = new JsonStorage(optionalFilePath);
      String key = "TestKey1";
      ObjectNode values = obj.getOMObject().createObjectNode();
      values.put("name", "apple");
      values.put("name", "orange");
      values.put("name", "kiwi");
      String response = obj.create(key, values);
      System.out.println(response);

      /*READ Operation*/
      String key1 = "TestKey";
      String response1 = obj.read(key1);
      System.out.println(response1);
      
      /* DELETE Operation*/
      /*String key2 = "TestKey";
      String response2 = obj.delete(key2);
      System.out.println(response2);*/
      }
      }
      </pre>
