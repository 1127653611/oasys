package cn.oasys.web.model.dao.file;


import cn.oasys.web.model.pojo.file.AoaFilePath;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AoaFilePathMapper {

    AoaFilePath findByPathName(String userName);

    void insertSelective(AoaFilePath filepath1);

     List<AoaFilePath> findpathByparent(@Param("id") Long parentId, @Param("path_istrash") Long ist);

    AoaFilePath findOne(Long pathid);

    List<AoaFilePath> findByPathNameAndParentId(@Param("name") String name, @Param("pid") Long pathId);

    List<AoaFilePath> findByParentId(Long pathid);

    void updateByPrimaryKey(AoaFilePath filepath);

    List<AoaFilePath> findByPathUserIdAndPathIstrash(@Param("uid") Long userId, @Param("trash") long istrash);

    AoaFilePath findByParentIdAndPathUserId(@Param("pid") long parentId, @Param("uid") Long userid);

    List<AoaFilePath> findByParentIdAndPathIstrash(@Param("pid") Long parentid, @Param("trash") long istrash);

    void delete(Long filepathid);

    List<AoaFilePath> findByPathUserIdAndPathIstrashAndPathNameLikeAndParentIdNot(@Param("uid") Long userId, @Param("trash") long istrash, @Param("key") String findlike, @Param("pid") long paretid);
}