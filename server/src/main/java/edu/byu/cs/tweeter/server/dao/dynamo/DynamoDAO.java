package edu.byu.cs.tweeter.server.dao.dynamo;

import edu.byu.cs.tweeter.server.dao.dynamo.tables.DynamoTweeterTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbIndex;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

abstract public class DynamoDAO<T extends DynamoTweeterTable> {
//    abstract Class<T> getTableClass();
//
//    abstract String getTableName();
//    abstract Key buildKey(String partitionValue, Object sortKey);

    // DynamoDB client
    private static DynamoDbClient dynamoDbClient;

    private static DynamoDbEnhancedClient enhancedClient;


    protected static boolean isNonEmptyString(String value) {
        return (value != null && value.length() > 0);
    }

    protected DynamoDbClient getDynamoClient(){
        if(dynamoDbClient == null){
            System.out.println("making dynamoDbClient...");
            dynamoDbClient = DynamoDbClient.builder()
                    .region(Region.US_EAST_1)
                    .build();
            System.out.println("finished making dynamoDbClient...");
        }
        return dynamoDbClient;
    }

    protected DynamoDbEnhancedClient getEnhancedDynamoClient(){
        if(enhancedClient == null){
            System.out.println("making enhancedClient...");
            enhancedClient = DynamoDbEnhancedClient.builder()
                    .dynamoDbClient(getDynamoClient())
                    .build();
            System.out.println("finished making enhancedClient...");
        }
        return enhancedClient;
    }
//
//    /**
//     * Retrieves a single item from database with a given partition key value and sort key.
//     *
//     * @param partitionValue the desired value of the partition key for items to be returned
//     * @param sortKey the desired value of the sort key for items to be returned
//     * @return The item matching the desired partition key and sort key
//     */
//    protected T find(String tableName, Class<T> tableClass, String partitionValue, Object sortKey){
//        try{
//            // TODO: figure out how to handle queries that only use partition keys (e.g. get User by alias)
//            DynamoDbTable<T> table = enhancedClient.table(tableName, TableSchema.fromBean(tableClass));
//            Key key = buildKey(partitionValue, sortKey);
//
//            // load it if it exists
//            T existingEntry = table.getItem(key);
////        QuerySpec spec = new QuerySpec()
////                .withKeyConditionExpression("Id = :v_id")
////                .withValueMap(new ValueMap()
////                        .withString(":v_id", "Amazon DynamoDB#DynamoDB Thread 1"));
////
////        ItemCollection<QueryOutcome> items = table.query(spec);
////
////        Iterator<Item> iterator = items.iterator();
////        Item item = null;
////        while (iterator.hasNext()) {
////            item = iterator.next();
////            System.out.println(item.toJSONPretty());
////        }
//            return existingEntry;
//        }catch (Exception e){
//            throw new RuntimeException("[DAO Error] Error thrown while fetching " + getTableClass().toString() + " from database (partitionValue=" + partitionValue.toString() + ", sortKey=" + sortKey + "): " + e.getMessage());
//        }
//    }
//
//    /**
//     * Retrieves a list of items from database that match a given partition key value.
//     *
//     * @param partitionValue the desired value of the partition key for items to be returned
//     * @return The item matching the desired partition key
//     */
//    protected List<T> fetchAll(String tableName, Class<T> tableClass, String partitionValue){
//        try{
//            // TODO: figure out how to handle queries that only use partition keys (e.g. get User by alias)
//            DynamoDbTable<T> table = enhancedClient.table(tableName, TableSchema.fromBean(tableClass));
////            QueryEnhancedRequest query = QueryEnhancedRequest.builder().
//            Key key = Key.builder()
//                    .partitionValue(partitionValue)
//                    .build();
//
////            TableKeysAndAttributes forumTableKeysAndAttributes = new TableKeysAndAttributes(forumTableName);
////            forumTableKeysAndAttributes.addHashOnlyPrimaryKeys("Name",
////                    "Amazon S3",
////                    "Amazon DynamoDB");
//
//            // load it if it exists
////            T existingEntry = table.query(key);
////        QuerySpec spec = new QuerySpec()
////                .withKeyConditionExpression("Id = :v_id")
////                .withValueMap(new ValueMap()
////                        .withString(":v_id", "Amazon DynamoDB#DynamoDB Thread 1"));
////
////        ItemCollection<QueryOutcome> items = table.query(spec);
////
////        Iterator<Item> iterator = items.iterator();
////        Item item = null;
////        while (iterator.hasNext()) {
////            item = iterator.next();
////            System.out.println(item.toJSONPretty());
////        }
////            return existingEntry;
//        }catch (Exception e){
//            throw new RuntimeException("[DAO Error] Error thrown while fetching " + getTableClass().toString() + " from database (partitionValue=" + partitionValue.toString() + ", sortKey=" + sortKey + "): " + e.getMessage());
//        }
//    }
//
//
//    protected void insert(String tableName, Class<T> tableClass, T entry) {
//        try{
//            DynamoDbTable<T> table = enhancedClient.table(tableName, TableSchema.fromBean(tableClass));
//            Key key = buildKey(entry.partitionValue(), entry.sortKey());
////        Key key = Key.builder()
////                .partitionValue(partitionValue).sortValue(sortValue)
////                .build();
//
//            // checks if it already exists
//            T existingEntry = table.getItem(key);
//            if (existingEntry == null) {
//                table.putItem(entry);
//            }
//        }catch (Exception e){
//            throw new RuntimeException("[DAO Error] Error thrown while inserting " + getTableClass().toString() + " into database (partitionValue=" + entry.partitionValue() + ", sortKey=" + entry.sortKey().toString() + "): " + e.getMessage());
//        }
//    }
//
//    public void update(String tableName, Class<T> tableClass, T objectToUpdate) {
//        try{
//            DynamoDbTable<T> table = enhancedClient.table(tableName, TableSchema.fromBean(tableClass));
//            Key key = buildKey(objectToUpdate.partitionValue(), objectToUpdate.sortKey());
//
//            // load it if it exists
//            T existingEntry = table.getItem(key);
//            if (existingEntry != null) {
//                table.updateItem(objectToUpdate);
//            }
//        }catch (Exception e){
//            throw new RuntimeException("[DAO Error] Error thrown while updating " + getTableClass().toString() + " in database (partitionValue=" + objectToUpdate.partitionValue() + ", sortKey=" + objectToUpdate.sortKey().toString() + "): " + e.getMessage());
//        }
//    }
//
//    public void createOrUpdate(String tableName, Class<T> tableClass, T newEntry) {
//        try{
//            DynamoDbTable<T> table = enhancedClient.table(tableName, TableSchema.fromBean(tableClass));
//            Key key = buildKey(newEntry.partitionValue(), newEntry.sortKey());
//
//            // load it if it exists
//            T existingEntry = table.getItem(key);
//            if (existingEntry == null) {
//                table.putItem(newEntry);
//            } else {
//                table.updateItem(newEntry);
//            }
//        }catch (Exception e){
//            throw new RuntimeException("[DAO Error] Error thrown while performing createOrUpdate for " + getTableClass().toString() + " in database (partitionValue=" + newEntry.partitionValue() + ", sortKey=" + newEntry.sortKey().toString() + "): " + e.getMessage());
//        }
//    }
//
//    /**
//     * Delete all entries in the specified table with a given partitionValue and sortValue
//     *
//     * @param partitionValue
//     * @param sortValue
//     */
//    public void delete(String tableName, Class<T> tableClass, String partitionValue, Object sortValue) {
//        try{
//            DynamoDbTable<T> table = enhancedClient.table(tableName, TableSchema.fromBean(tableClass));
//            Key key = buildKey(partitionValue, sortValue);
//            table.deleteItem(key);
//        }catch (Exception e){
//            throw new RuntimeException("[DAO Error] Error thrown while deleting " + getTableClass().toString() + " from database (partitionValue=" + partitionValue + ", sortValue=" + sortValue + "): " + e.getMessage());
//        }
//    }
}



