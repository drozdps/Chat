package com.drozdps.chat.configuration;

import java.util.Arrays;
import org.springframework.cassandra.core.keyspace.CreateKeyspaceSpecification;
import org.springframework.context.annotation.Bean;
import org.springframework.data.cassandra.config.java.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.mapping.BasicCassandraMappingContext;
import org.springframework.data.cassandra.mapping.CassandraMappingContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean;

@Profile({"dev","test"})
@Configuration
public class CassandraConfiguration extends AbstractCassandraConfiguration {
	private static final String DB_NAME = "chat";
	private static final String USE_CLUSTER = "USE chat";
	private static final String CLUSTER_REQUEST = "CREATE TABLE IF NOT EXISTS message (" +
										"username text," +
										"date timestamp," +
										"fromUser text," +
										"roomId text," +
										"toUser text," +
										"text text," +
										"PRIMARY KEY ((username, roomId), date)" +
									") WITH CLUSTERING ORDER BY (date ASC)";
	private static final String LOCALHOST = "localhost";
	private static final int PORT = 9042;
	
    @Bean
    @Override
    public CassandraClusterFactoryBean cluster() {
    	
        CassandraClusterFactoryBean cluster = new CassandraClusterFactoryBean();
        cluster.setPort(PORT);
        cluster.setContactPoints(LOCALHOST);
        cluster.setKeyspaceCreations(
        		Arrays.asList(
        				new CreateKeyspaceSpecification(DB_NAME)
        				.ifNotExists()
        				.withSimpleReplication(1))
        		);
        
        cluster.setStartupScripts(Arrays.asList(USE_CLUSTER, CLUSTER_REQUEST));
        return cluster;
    }
    @Override
    protected String getKeyspaceName() {
        return DB_NAME;
    }
    
    @Bean
    @Override
    public CassandraMappingContext cassandraMapping() 
      throws ClassNotFoundException {
        return new BasicCassandraMappingContext();
    }
    


}
