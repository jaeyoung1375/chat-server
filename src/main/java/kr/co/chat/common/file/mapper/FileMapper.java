package kr.co.chat.common.file.mapper;

import org.apache.ibatis.annotations.Mapper;
import kr.co.chat.common.file.dto.FileDto;


@Mapper
public interface FileMapper {

	void insertFile(FileDto file);

	FileDto getFile(Long fileId);

}
