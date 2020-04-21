package com.varrock.tools;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.query.*;
import com.couchbase.client.java.query.QueryResult;

public class CouchbaseTest {

	public static void main(String[] args) {
		
		Cluster cluster = CouchbaseCluster.create("127.0.0.1");
		Bucket bucket = cluster.openBucket();
		
		JsonDocument json = bucket.get("u:graham");
		System.out.println(json.toString());
		
		
		QueryResult result = null;
		Query.simple("ok");
		//QueryResult result = bucket.query(Query.simple("SELECT COUNT(*) AS `count` FROM `default` WHERE META(default).id = 'u:apache_ah64'"));
		
		//System.out.println(result.allRows().get(0).value());

		cluster.disconnect();
		
	}
	
}
