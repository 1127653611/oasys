package cn.oasys.web.model.dao.file;

import cn.oasys.web.model.pojo.file.AoaFileList;
import cn.oasys.web.model.pojo.file.AoaFilePath;
import cn.oasys.web.model.pojo.user.AoaUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AoaFileListMapper {
    void insertSelective(AoaFileList filelist);

    List<AoaFileList> findByFilePath(Long pathid);

    List<AoaFileList> findByFileNameAndFpath(@Param("name") String name, @Param("pid") Long pathid);

    AoaFileList finOne(Long fileid);

    void updateByPrimaryKey(AoaFileList fileList);

    List<AoaFileList> finddocument(Long userId);

    List<AoaFileList> findByUserAndContentTypeLikeAndFileIstrash(@Param("uid") Long userId, @Param("key") String contenttype, @Param("trash") long istrash);

    List<AoaFileList> findByUserAndFileIstrash(@Param("uid") Long userId, @Param("trash") long istrash);

    List<AoaFileList> findByFileIsshareAndFileIstrash(@Param("share") long isshare, @Param("trash") long istrash);

    List<AoaFileList> findByPath(Long pathId);

    void delete(Long fileid);

    List<AoaFileList> finddocumentlike(@Param("id") Long userId, @Param("key") String findlike);

    List<AoaFileList> findByUserAndFileIstrashAndContentTypeLikeAndFileNameLike(@Param("uid") Long userId, @Param("trash") long istrash, @Param("key") String contenttype, @Param("key1") String findlike);

    List<AoaFileList> findByFileIsshareAndFileNameLike(@Param("share") long share, @Param("key") String findlike);

    List<AoaFileList> findByUserAndFileIstrashAndFileNameLike(@Param("uid") Long userId, @Param("trash") long istrash, @Param("key") String findlike);

    @Select("select count(*) from aoa_file_list")
    Long count();
}