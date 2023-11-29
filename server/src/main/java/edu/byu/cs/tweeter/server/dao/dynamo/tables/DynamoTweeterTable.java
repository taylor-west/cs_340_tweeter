package edu.byu.cs.tweeter.server.dao.dynamo.tables;

public interface DynamoTweeterTable {

    /**
     * Gets the name of the table in DynamoDB that contains the items being accessed.
     * @return the name of the DynamoDB table
     */
    String tableName();

    /**
     * Gets the name of the primary index (partition key) for the DynamoDB table.
     * @return the name of the primary index for the DynamoDB table.
     */
    String indexName();

    /**
     * Gets the name of the attribute that is set to be the primary index key (partition key) for the DynamoDB table.
     * @return the name of the attribute that is set to be the primary index/partition key for the table.
     */
    String indexAttributeName();

    /**
     * Gets the name of the attribute that is the sort key for the primary index of the DynamoDB table.
     * @return the name of the attribute that is the sort key for the primary index of the table.
     */
    String sortAttributeName();

    /**
     * Gets the name of the secondary index for the DynamoDB table.
     * @return the name of the secondary index for the table.
     */
    String secondaryIndexName();

    /**
     * Gets the name of the attribute that is the index key for secondary index of the DynamoDB table.
     * @return the name of the attribute that is the index key for secondary index of the table.
     */
    String secondaryIndexAttributeName();

    /**
     * Gets the name of the attribute that is the sort key for the secondary index of the DynamoDB table.
     * @return the name of the attribute that is the sort key for the secondary index of the table.
     */
    String secondarySortAttributeName();
}
