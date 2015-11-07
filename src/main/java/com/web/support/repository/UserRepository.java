package com.web.support.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserRepository<K1, K2, T> {

	private final Logger logger = LoggerFactory.getLogger(UserRepository.class);

	private List<T> goodsList = new LinkedList<T>();
	private Map<K1, Map<K2, UserGoods<K1, K2>>> repository = new HashMap<K1, Map<K2, UserGoods<K1, K2>>>();

	public interface UserGoods<K1, K2> {

		public K1 userKey();

		public K2 goodsKey();

	}

	public void put(UserGoods<K1, K2> goods) {
		this.put(goods.userKey(), goods.goodsKey(), goods);
	}

	@SuppressWarnings("unchecked")
	public void put(K1 k1, K2 k2, UserGoods<K1, K2> goods) {
		Map<K2, UserGoods<K1, K2>> contentMap = this.repository.get(k1);
		if (contentMap == null) {
			contentMap = new HashMap<K2, UserGoods<K1, K2>>();
			this.repository.put(k1, contentMap);
		}
		contentMap.put(k2, goods);
		this.goodsList.add((T) goods);
	}

	public T get(UserGoods<K1, K2> goods) {
		return this.get(goods.userKey(), goods.goodsKey());
	}

	@SuppressWarnings("unchecked")
	public T get(K1 k1, K2 k2) {
		Map<K2, UserGoods<K1, K2>> contentMap = this.repository.get(k1);
		if (contentMap == null) {
			return null;
		}
		return (T) contentMap.get(k2);
	}

	public void remove(UserGoods<K1, K2> goods) {
		this.remove(goods.userKey(), goods.goodsKey());
	}

	public void remove(K1 k1, K2 k2) {
		Map<K2, UserGoods<K1, K2>> contentMap = this.repository.get(k1);
		if (contentMap != null) {
			UserGoods<K1, K2> content = contentMap.remove(k2);
			if (content != null) {
				this.goodsList.remove(content);
			}
			if (contentMap.size() == 0) {
				this.repository.remove(contentMap);
			}
			this.logger.debug("remove {}", content.toString());
		}
	}

	public List<T> getContent() {
		List<T> contentList = new ArrayList<T>();
		contentList.addAll(this.goodsList);
		return contentList;
	}

}
