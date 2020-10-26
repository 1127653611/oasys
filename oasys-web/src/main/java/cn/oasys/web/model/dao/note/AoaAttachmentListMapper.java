package cn.oasys.web.model.dao.note;

import cn.oasys.web.model.pojo.note.AoaAttachmentList;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AoaAttachmentListMapper {

    AoaAttachmentList findOne(long id);

    void insertSelective(AoaAttachmentList attachment);

    Integer updateByPrimaryKeySelective(AoaAttachmentList aoaAttachmentList);
}