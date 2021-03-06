package com.jstarcraft.nlp.bloomfilter;

import java.math.BigInteger;
import java.util.Random;

import com.jstarcraft.core.common.hash.StringHashFunction;

/**
 * 基于BigInteger的布隆过滤器
 * 
 * @author Birdy
 *
 */
public class BitNumberBloomFilter extends LocalBloomFilter<BigInteger> {

	private int capacity;

	public BitNumberBloomFilter(int bitSize, StringHashFamily hashFamily, int hashSize, Random random) {
		super(new BigInteger(new byte[bitSize / Byte.SIZE + (bitSize % Byte.SIZE == 0 ? 0 : 1)]), getFunctions(hashFamily, hashSize, random));
		this.capacity = bitSize;
	}

	@Override
	public boolean get(String data) {
		for (StringHashFunction function : functions) {
			int hash = function.hash(data);
			int index = Math.abs(hash % capacity);
			if (!bits.testBit(index)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void put(String data) {
		for (StringHashFunction function : functions) {
			int hash = function.hash(data);
			int index = Math.abs(hash % capacity);
			// TODO 每次都是拷贝,不建议使用.
			bits = bits.setBit(index);
		}
	}

}
