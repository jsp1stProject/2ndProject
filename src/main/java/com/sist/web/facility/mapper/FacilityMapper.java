package com.sist.web.facility.mapper;
import java.util.*;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.sist.web.facility.vo.FacilityVO;

@Mapper
public interface FacilityMapper {
	
	@Select("SELECT DISTINCT CTGRY_TWO_NM FROM P_PET_FACILITY ORDER BY CTGRY_TWO_NM")
	public List<String> categoryList();

	
	@Select("SELECT * FROM ( " +
	        " SELECT a.*, ROWNUM AS rn FROM ( " +
	        "   SELECT * FROM P_PET_FACILITY " +
	        "   WHERE CTGRY_TWO_NM = #{category} " +
	        "   ORDER BY FCLTY_NM " +
	        " ) a " +
	        " WHERE ROWNUM <= #{end} " +
	        ") WHERE rn >= #{start}")

    public List<FacilityVO> facilityList(@Param("category") String category,
                                               @Param("start") int start,
                                               @Param("end") int end);

    // 선택된 카테고리의 총 페이지 수
    @Select("SELECT CEIL(COUNT(*) / 20.0) FROM P_PET_FACILITY WHERE CTGRY_TWO_NM = #{category}")
    public int facilityTotalPage(@Param("category") String category);
    
    
    @Select("SELECT * FROM ( " +
	        " SELECT a.*, ROWNUM AS rn FROM ( " +
	        "   SELECT * FROM P_PET_FACILITY " +
	        "   WHERE CTGRY_TWO_NM = #{category} " +
	        "   AND signgu_nm LIKE '%' || #{location} || '%' " +
	        "   ORDER BY FCLTY_NM " +
	        " ) a " +
	        " WHERE ROWNUM <= #{end} " +
	        ") WHERE rn >= #{start}")
    		List<FacilityVO> facilityListFiltered(
    		    @Param("category") String category,
    		    @Param("location") String location,
    		    @Param("start") int start,
    		    @Param("end") int end
    		);

    		@Select("SELECT COUNT(*) FROM P_PET_FACILITY "
    		+"WHERE CTGRY_TWO_NM = #{category} "
    		+"AND signgu_nm LIKE '%' || #{location} || '%' ")
    		int facilityTotalPageFiltered(@Param("category") String category,@Param("location") String location);

    
	
    // 디테일 수정할거있나
    @Select("SELECT * FROM P_PET_FACILITY WHERE FCLTY_NM = #{FCLTY_NM}")
    public FacilityVO facilityDetail();

    // 반경 5km 내의 시설 찾기
    @Select("SELECT FCLTY_NM AS name, RDNMADR_NM AS address, TEL_NO AS tel, " +
            "TO_NUMBER(LC_LA) AS lat, TO_NUMBER(LC_LO) AS lon " +
            "FROM P_PET_FACILITY " +
            "WHERE LC_LA IS NOT NULL AND LC_LO IS NOT NULL " +
            "AND 6371 * ACOS(" +
            "COS(RADIANS(#{lat})) * COS(RADIANS(TO_NUMBER(LC_LA))) * " +
            "COS(RADIANS(TO_NUMBER(LC_LO)) - RADIANS(#{lon})) + " +
            "SIN(RADIANS(#{lat})) * SIN(RADIANS(TO_NUMBER(LC_LA)))" +
            ") < 5") // 반경 5km
    List<Map<String, Object>> findNearbyFacilities(double lat, double lon);
}
