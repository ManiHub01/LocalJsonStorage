# LocalJsonStorage

 To store the dataS based on key and Value please follow the bellow steps 

 1) crate one Java Project then 

 2) add this Jar file into your buidPath Fwbacukendtask-0.0.1-SNAPSHOT

 3) create one Java file. copy and past the below snippet into your class.


      import com.fasterxml.jackson.databind.node.ObjectNode;
      import com.mypack.JsonStorage;
      public class FwStorageTest {
        public static void main(String[] args) {
          /* CREATE Operation*/
      /*String optionalFilePath = "";
      JsonStorage obj = new JsonStorage(optionalFilePath);
      String key = "TestKey1";
      ObjectNode values = obj.getOMObject().createObjectNode();
      values.put("name", "Raja");
      values.put("name", "Ravi");
      values.put("name", "Mani");
      String response = obj.create(key, values);
      System.out.println(response);*/

      /* READ Operation*/
      /*String key1 = "TestKey";
      String response1 = obj.read(key1);
      System.out.println(response1);
      */		

      /* DELETE Operation*/
      /*String key2 = "TestKey";
      String response2 = obj.delete(key2);
      System.out.println(response2);*/
      }
      }
