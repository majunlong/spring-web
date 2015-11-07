package com.web.model;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.apache.commons.codec.digest.DigestUtils;

import com.web.support.repository.UserRepository.UserGoods;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = false, exclude = { "buffer", "channel" })
@EqualsAndHashCode(callSuper = false, exclude = { "id", "username", "timeStamp", "buffer", "channel" })
public class FileHandler implements UserGoods<String, String> {

	private String id;
	private String username;
	private String name;
	private String type;
	private Long block = 0L;
	private Long size;
	private Long modifyTime;
	private Long timeStamp;
	private ByteBuffer buffer;
	private FileChannel channel;

	public FileHandler(String id, Long block) {
		super();
		this.id = id;
		this.block = block;
	}

	@Override
	public String userKey() {
		return this.username;
	}

	@Override
	public String goodsKey() {
		return this.id;
	}

	public synchronized boolean write(String fileContent) throws FileNotFoundException, IOException {
		this.timeStamp = System.currentTimeMillis();
		String[] contentChars = fileContent.split(",");
		byte[] contentBytes = new byte[contentChars.length];
		for (int i = 0; i < contentBytes.length; i++) {
			contentBytes[i] = Byte.parseByte(contentChars[i]);
		}
		this.block += contentBytes.length;
		if (this.buffer == null) {
			this.buffer = ByteBuffer.allocate(1179648);
		}
		this.buffer.put(contentBytes);
		if (this.buffer.position() > 1048576) {
			this.buffer.flip();
			if (this.channel == null) {
				this.channel = new FileOutputStream("C:\\Users\\Administrator\\Desktop\\[" + this.id + "]" + this.name, true).getChannel();
			}
			this.channel.write(this.buffer);
			this.buffer.clear();
		}
		return false;
	}

	public synchronized void close() {
		try {
			if (this.channel == null) {
				this.channel = new FileOutputStream("C:\\Users\\Administrator\\Desktop\\[" + this.id + "]" + this.name, true).getChannel();
			}
			if (this.channel != null) {
				if (this.buffer.position() > -1) {
					this.buffer.flip();
					this.channel.write(this.buffer);
				}
				this.channel.close();
				this.buffer = null;
				this.channel = null;
			}
		} catch (IOException e) {
		}
	}

	public void setId(String id) {
		this.id = DigestUtils.md5Hex(DigestUtils.md5Hex(id) + this.name + this.type + this.size + this.modifyTime).toUpperCase();
	}

}
