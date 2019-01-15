package me.axim;

import me.axim.annotation.Begin;
import me.axim.annotation.Test;
import me.axim.annotation.TestClass;

@TestClass
public class Annotated {
    private String field1;
    public String field2;

    @Begin
    public void beginTest() {
        System.out.println("Begin");
    }

    @Test
    private void test1() {
        System.out.println("test1");
    }

    @Test
    public void test2() {
        System.out.println("test2");
    }
}
