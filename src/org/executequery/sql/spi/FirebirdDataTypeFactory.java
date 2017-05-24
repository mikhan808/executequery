package org.executequery.sql.spi;

import liquibase.database.Database;
import liquibase.datatype.DataTypeFactory;
import liquibase.datatype.LiquibaseDataType;
import liquibase.datatype.core.UnknownType;
import liquibase.datatype.core.VarcharType;

/**
 * Created by vasiliy on 17.01.17.
 */
public class FirebirdDataTypeFactory extends DataTypeFactory {

    private static FirebirdDataTypeFactory instance;

    FirebirdDataTypeFactory () {
        super();
    }

    @Override
    public LiquibaseDataType fromDescription(String dataTypeDefinition, Database database) {

        if (dataTypeDefinition.contains("COMPUTED BY")) {
//            LiquibaseDataType type = new VarcharType();
            Object type = new UnknownType(dataTypeDefinition);
            ((LiquibaseDataType)type).finishInitialization(dataTypeDefinition);
            return (LiquibaseDataType)type;
        }

        return super.fromDescription(dataTypeDefinition, database);
    }

    public static synchronized FirebirdDataTypeFactory getFBInstance() {
        if(instance == null) {
            instance = new FirebirdDataTypeFactory();
        }

        return instance;
    }
}
