package com.lunar.hwpx.sample;

import com.lunar.hwpx.reader.HWPXReader;

public class SampleMain {

	public static void main(String[] args) {

		String text = HWPXReader.readText("sample/안녕하세요.hwpx");
		System.out.println(text);
	}
}
