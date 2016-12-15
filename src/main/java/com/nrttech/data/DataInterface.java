package com.nrttech.data;
 

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.StatementType;

public interface DataInterface {

//	@Select("SELECT * FROM AKBMKEYS WHERE ID=#{id}")
//	List<AkbMasterKey> queryAkbMasterKeyById( @Param ("id") String id );
	
	//@Insert("INSERT INTO AKBMKEYS(ID,VARIANT,KEYCURR,UPDEPOCH,COMMENT)VALUES(#{id},#{variant},#{keycurr},#{updepoch},#{comment})")
	//int insertAkbMasterKeyById( AkbMasterKey key );
	//@Insert("INSERT INTO AKBMKEYS(ID,VARIANT,KEYCURR,UPDEPOCH,COMMENT)VALUES(#{id},#{variant},#{keycurr},#{updepoch},#{comment})")
//	@Insert(value = { 
//            "{ call usp_dev_InsertCokeDeviceMasterKey ( #{car_id, mode=IN, jdbcType=VARCHAR, javaType=String },  #{car_status, mode=IN, jdbcType=VARCHAR, javaType=String },  #{result_code, mode=OUT, jdbcType=NUMERIC, javaType=Integer},  #{error_description, mode=OUT, jdbcType=VARCHAR, javaType=String} ) }"
//            })
	@Insert(value = { 
			"{ call usp_dev_InsertCokeDeviceMasterKey ( #{sn},   #{key} ) }"
	})
    @Options(statementType = StatementType.CALLABLE)
	int insertAkbMasterKeyById( Map<String, Object> parameters );
	
//	@Delete("DELETE FROM AKBMKEYS  WHERE ID=#{id}")
//	int deleteAkbMasterKeyById( @Param ("id") String id );

}