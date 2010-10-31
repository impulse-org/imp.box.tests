/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Robert Fuhrer (rfuhrer@watson.ibm.com) - initial API and implementation
 *******************************************************************************/
package org.eclipse.imp.box.interpreter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.eclipse.imp.box.parser.BoxParseController;
import org.eclipse.imp.box.parser.Ast.IBox;
import org.eclipse.imp.utils.SystemOutErrMessageHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

/**
 * @author rfuhrer
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class BoxInterpreterTest {
    // TODO Don't bother creating the box program as a string, just create a Box AST
    // Turning everything into a String causes ambiguity in emitOp() below -- it
    // can't assume that "H hs =" isn't a valid fragment in the target language.
 
    // no-wrap tests -- basically, tests that don't use the HV, HOV or G operators

    @Test
    public void horizTestA_10_4_true() {
        runTest(10, true, 4, H("abcdef", "ghijkl", "mnopqr"), "abcdef ghijkl mnopqr");
    }

    @Test
    public void horizTestA_20_4_true() {
        runTest(20, true, 4, H("abcdef", "ghijkl", "mnopqr"), "abcdef ghijkl mnopqr");
    }

    @Test
    public void horizTestA_40_4_true() {
        runTest(40, true, 4, H("abcdef", "ghijkl", "mnopqr"), "abcdef ghijkl mnopqr");
    }

    @Test
    public void horizTestA_60_4_true() {
        runTest(60, true, 4, H("abcdef", "ghijkl", "mnopqr"), "abcdef ghijkl mnopqr");
    }

    @Test
    public void horizTestA_80_4_true() {
        runTest(80, true, 4, H("abcdef", "ghijkl", "mnopqr"), "abcdef ghijkl mnopqr");
    }

    @Test
    public void horizTestA_10_4_true_0() {
        runTest(10, true, 4, H(0, "abcdef", "ghijkl", "mnopqr"), "abcdefghijklmnopqr");
    }

    @Test
    public void horizTestA_20_4_true_0() {
        runTest(20, true, 4, H(0, "abcdef", "ghijkl", "mnopqr"), "abcdefghijklmnopqr");
    }

    @Test
    public void horizTestA_40_4_true_0() {
        runTest(40, true, 4, H(0, "abcdef", "ghijkl", "mnopqr"), "abcdefghijklmnopqr");
    }

    @Test
    public void horizTestA_60_4_true_0() {
        runTest(60, true, 4, H(0, "abcdef", "ghijkl", "mnopqr"), "abcdefghijklmnopqr");
    }

    @Test
    public void horizTestA_20_4_true_2() {
        runTest(20, true, 4, H(2, "abcdef", "ghijkl", "mnopqr"), "abcdef  ghijkl  mnopqr");
    }

    @Test
    public void horizTestA_20_4_true_3() {
        runTest(20, true, 4, H(3, "abcdef", "ghijkl", "mnopqr"), "abcdef   ghijkl   mnopqr");
    }

    // ============================================================================================

    @Test
    public void vertTestA_20_4_true() {
        runTest(20, true, 4, V("abcdef", "ghijkl", "mnopqr"), "abcdef\nghijkl\nmnopqr");
    }

    @Test
    public void vertTestA_20_4_true_1() {
        runTest(20, true, 4, V(1, "abcdef", "ghijkl", "mnopqr"), "abcdef\n\nghijkl\n\nmnopqr");
    }

    @Test
    public void vertTestA_20_4_true_2() {
        runTest(20, true, 4, V(2, "abcdef", "ghijkl", "mnopqr"), "abcdef\n\n\nghijkl\n\n\nmnopqr");
    }

    @Test
    public void vertTestA_20_4_true_3() {
        runTest(20, true, 4, V(3, "abcdef", "ghijkl", "mnopqr"), "abcdef\n\n\n\nghijkl\n\n\n\nmnopqr");
    }

    @Test
    public void vertTestB_20_4_true_0() {
        runTest(20, true, 4, V(H(5,
                                 H(2, "public", "static", "def", "main(Array[String])"), "{"),
                                     I(8, "Console.OUT.println(Hello, World!);"),
                                 "}"),
                                 "public  static  def  main(Array[String])     {\n" +
                                 "        Console.OUT.println(Hello, World!);\n" +
                                 "}");
    }

    @Test
    public void vertTestB_20_4_true_1() {
        runTest(20, true, 4, V(1,
                                H(5, H(2, "public", "static", "def", "main(Array[String])"), "{"),
                                    I(8, "Console.OUT.println(Hello, World!);"),
                                "}"),
                                "public  static  def  main(Array[String])     {\n\n" +
                                 "        Console.OUT.println(Hello, World!);\n\n" +
                                 "}");
    }

    @Test
    public void vertTestB_20_4_true_2() {
        runTest(20, true, 4, V(2,
                                H(5, H(2, "public", "static", "def", "main(Array[String])"), "{"),
                                    I(8, "Console.OUT.println(Hello, World!);"),
                                "}"),
                                "public  static  def  main(Array[String])     {\n\n\n" +
                                 "        Console.OUT.println(Hello, World!);\n\n\n" +
                                 "}");
    }

    // ============================================================================================

    @Test
    public void wrapTest_HOV_A_20_true_1() {
        runTest(20, true, 4,
                HOV("abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd"),
                "abcd\nabcd\nabcd\nabcd\nabcd\nabcd\nabcd\nabcd\nabcd\nabcd");
    }

    @Test
    public void wrapTest_HOV_A_20_true_2() {
        runTest(20, true, 4,
                HOV(2, 0, "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd"),
                "abcd\nabcd\nabcd\nabcd\nabcd\nabcd\nabcd\nabcd\nabcd\nabcd");
    }

    @Test
    public void wrapTest_HOV_A_20_true_3() {
        runTest(20, true, 4,
                HOV(3, 0, "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd"),
                "abcd\nabcd\nabcd\nabcd\nabcd\nabcd\nabcd\nabcd\nabcd\nabcd");
    }

    @Test
    public void wrapTest_HOV_A_40_true_0() {
        runTest(40, true, 4,
                HOV(0, 0, "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd"),
                "abcdabcdabcdabcdabcdabcdabcdabcdabcdabcd");
    }

    @Test
    public void wrapTest_HOV_A_40_true_1() {
        runTest(40, true, 4,
                HOV("abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd"),
                "abcd\nabcd\nabcd\nabcd\nabcd\nabcd\nabcd\nabcd\nabcd\nabcd");
    }

    @Test
    public void wrapTest_HOV_A_40_true_2() {
        runTest(40, true, 4,
                HOV(2, 0, "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd"),
                "abcd\nabcd\nabcd\nabcd\nabcd\nabcd\nabcd\nabcd\nabcd\nabcd");
    }

    @Test
    public void wrapTest_HOV_A_40_true_3() {
        runTest(40, true, 4,
                HOV(3, 0, "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd"),
                "abcd\nabcd\nabcd\nabcd\nabcd\nabcd\nabcd\nabcd\nabcd\nabcd");
    }

    @Test
    public void wrapTest_HOV_A_60_true_0() {
        runTest(60, true, 4,
                HOV(0, 0, "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd"),
                "abcdabcdabcdabcdabcdabcdabcdabcdabcdabcd");
    }

    @Test
    public void wrapTest_HOV_A_60_true_1() {
        runTest(60, true, 4,
                HOV("abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd"),
                "abcd abcd abcd abcd abcd abcd abcd abcd abcd abcd");
    }

    @Test
    public void wrapTest_HOV_A_60_true_2() {
        runTest(60, true, 4,
                HOV(2, 0, "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd"),
                "abcd  abcd  abcd  abcd  abcd  abcd  abcd  abcd  abcd  abcd");
    }

    @Test
    public void wrapTest_HOV_A_60_true_3() {
        runTest(60, true, 4,
                HOV(3, 0, "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd"),
                "abcd\nabcd\nabcd\nabcd\nabcd\nabcd\nabcd\nabcd\nabcd\nabcd");
    }

    @Test
    public void wrapTest_HOV_A_80_true_0() {
        runTest(80, true, 4,
                HOV(0, 0, "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd"),
                "abcdabcdabcdabcdabcdabcdabcdabcdabcdabcd");
    }

    @Test
    public void wrapTest_HOV_A_80_true_1() {
        runTest(80, true, 4,
                HOV("abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd"),
                "abcd abcd abcd abcd abcd abcd abcd abcd abcd abcd");
    }

    @Test
    public void wrapTest_HOV_A_80_true_2() {
        runTest(80, true, 4,
                HOV(2, 0, "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd"),
                "abcd  abcd  abcd  abcd  abcd  abcd  abcd  abcd  abcd  abcd");
    }

    @Test
    public void wrapTest_HOV_A_80_true_3() {
        runTest(80, true, 4,
                HOV(3, 0, "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd"),
                "abcd   abcd   abcd   abcd   abcd   abcd   abcd   abcd   abcd   abcd");
    }

    // ============================================================================================

    @Test
    public void wrapTest_HOV_B_20_true_0() {
        runTest(20, true, 4,
                HOV(0, 0, "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"),
                "a\nb\nc\nd\ne\nf\ng\nh\ni\nj\nk\nl\nm\nn\no\np\nq\nr\ns\nt\nu\nv\nw\nx\ny\nz");
    }

    @Test
    public void wrapTest_HOV_B_20_true_1() {
        runTest(20, true, 4,
                HOV("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"),
                "a\nb\nc\nd\ne\nf\ng\nh\ni\nj\nk\nl\nm\nn\no\np\nq\nr\ns\nt\nu\nv\nw\nx\ny\nz");
    }

    @Test
    public void wrapTest_HOV_B_20_true_2() {
        runTest(20, true, 4,
                HOV(2, 0, "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"),
                "a\nb\nc\nd\ne\nf\ng\nh\ni\nj\nk\nl\nm\nn\no\np\nq\nr\ns\nt\nu\nv\nw\nx\ny\nz");
    }

    @Test
    public void wrapTest_HOV_B_20_true_3() {
        runTest(20, true, 4,
                HOV(3, 0, "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"),
                "a\nb\nc\nd\ne\nf\ng\nh\ni\nj\nk\nl\nm\nn\no\np\nq\nr\ns\nt\nu\nv\nw\nx\ny\nz");
    }

    @Test
    public void wrapTest_HOV_B_20_true_4() {
        runTest(20, true, 4,
                HOV(4, 0, "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"),
                "a\nb\nc\nd\ne\nf\ng\nh\ni\nj\nk\nl\nm\nn\no\np\nq\nr\ns\nt\nu\nv\nw\nx\ny\nz");
    }

    @Test
    public void wrapTest_HOV_B_40_true_0() {
        runTest(40, true, 4,
                HOV(0, 0, "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"),
                "abcdefghijklmnopqrstuvwxyz");
    }

    @Test
    public void wrapTest_HOV_B_40_true_1() {
        runTest(40, true, 4,
                HOV("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"),
                "a\nb\nc\nd\ne\nf\ng\nh\ni\nj\nk\nl\nm\nn\no\np\nq\nr\ns\nt\nu\nv\nw\nx\ny\nz");
    }

    @Test
    public void wrapTest_HOV_B_40_true_2() {
        runTest(40, true, 4,
                HOV(2, 0, "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"),
                "a\nb\nc\nd\ne\nf\ng\nh\ni\nj\nk\nl\nm\nn\no\np\nq\nr\ns\nt\nu\nv\nw\nx\ny\nz");
    }

    @Test
    public void wrapTest_HOV_B_40_true_3() {
        runTest(40, true, 4,
                HOV(3, 0, "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"),
                "a\nb\nc\nd\ne\nf\ng\nh\ni\nj\nk\nl\nm\nn\no\np\nq\nr\ns\nt\nu\nv\nw\nx\ny\nz");
    }

    @Test
    public void wrapTest_HOV_B_40_true_4() {
        runTest(40, true, 4,
                HOV(4, 0, "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"),
                "a\nb\nc\nd\ne\nf\ng\nh\ni\nj\nk\nl\nm\nn\no\np\nq\nr\ns\nt\nu\nv\nw\nx\ny\nz");
    }

    @Test
    public void wrapTest_HOV_B_60_true_0() {
        runTest(60, true, 4,
                HOV(0, 0, "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"),
                "abcdefghijklmnopqrstuvwxyz");
    }

    @Test
    public void wrapTest_HOV_B_60_true_1() {
        runTest(60, true, 4,
                HOV("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"),
                "a b c d e f g h i j k l m n o p q r s t u v w x y z");
    }

    @Test
    public void wrapTest_HOV_B_60_true_2() {
        runTest(60, true, 4,
                HOV(2, 0, "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"),
                "a\nb\nc\nd\ne\nf\ng\nh\ni\nj\nk\nl\nm\nn\no\np\nq\nr\ns\nt\nu\nv\nw\nx\ny\nz");
    }

    @Test
    public void wrapTest_HOV_B_60_true_3() {
        runTest(60, true, 4,
                HOV(3, 0, "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"),
                "a\nb\nc\nd\ne\nf\ng\nh\ni\nj\nk\nl\nm\nn\no\np\nq\nr\ns\nt\nu\nv\nw\nx\ny\nz");
    }

    @Test
    public void wrapTest_HOV_B_60_true_4() {
        runTest(60, true, 4,
                HOV(4, 0, "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"),
                "a\nb\nc\nd\ne\nf\ng\nh\ni\nj\nk\nl\nm\nn\no\np\nq\nr\ns\nt\nu\nv\nw\nx\ny\nz");
    }

    @Test
    public void wrapTest_HOV_B_80_true_0() {
        runTest(80, true, 4,
                HOV(0, 0, "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"),
                "abcdefghijklmnopqrstuvwxyz");
    }

    @Test
    public void wrapTest_HOV_B_80_true_1() {
        runTest(80, true, 4,
                HOV("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"),
                "a b c d e f g h i j k l m n o p q r s t u v w x y z");
    }

    @Test
    public void wrapTest_HOV_B_80_true_2() {
        runTest(80, true, 4,
                HOV(2, 0, "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"),
                "a  b  c  d  e  f  g  h  i  j  k  l  m  n  o  p  q  r  s  t  u  v  w  x  y  z");
    }

    @Test
    public void wrapTest_HOV_B_80_true_3() {
        runTest(80, true, 4,
                HOV(3, 0, "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"),
                "a\nb\nc\nd\ne\nf\ng\nh\ni\nj\nk\nl\nm\nn\no\np\nq\nr\ns\nt\nu\nv\nw\nx\ny\nz");
    }

    @Test
    public void wrapTest_HOV_B_80_true_4() {
        runTest(80, true, 4,
                HOV(4, 0, "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"),
                "a\nb\nc\nd\ne\nf\ng\nh\ni\nj\nk\nl\nm\nn\no\np\nq\nr\ns\nt\nu\nv\nw\nx\ny\nz");
    }

    @Test
    public void wrapTest_HOV_B_100_true_0() {
        runTest(100, true, 4,
                HOV(0, 0, "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"),
                "abcdefghijklmnopqrstuvwxyz");
    }

    @Test
    public void wrapTest_HOV_B_100_true_1() {
        runTest(100, true, 4,
                HOV("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"),
                "a b c d e f g h i j k l m n o p q r s t u v w x y z");
    }

    @Test
    public void wrapTest_HOV_B_100_true_2() {
        runTest(100, true, 4,
                HOV(2, 0, "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"),
                "a  b  c  d  e  f  g  h  i  j  k  l  m  n  o  p  q  r  s  t  u  v  w  x  y  z");
    }

    @Test
    public void wrapTest_HOV_B_100_true_3() {
        runTest(100, true, 4,
                HOV(3, 0, "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"),
                "a\nb\nc\nd\ne\nf\ng\nh\ni\nj\nk\nl\nm\nn\no\np\nq\nr\ns\nt\nu\nv\nw\nx\ny\nz");
    }

    @Test
    public void wrapTest_HOV_B_100_true_4() {
        runTest(100, true, 4,
                HOV(4, 0, "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"),
                "a\nb\nc\nd\ne\nf\ng\nh\ni\nj\nk\nl\nm\nn\no\np\nq\nr\ns\nt\nu\nv\nw\nx\ny\nz");
    }

    @Test
    public void wrapTest_HOV_B_120_true_0() {
        runTest(120, true, 4,
                HOV(0, 0, "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"),
                "abcdefghijklmnopqrstuvwxyz");
    }

    @Test
    public void wrapTest_HOV_B_120_true_1() {
        runTest(120, true, 4,
                HOV("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"),
                "a b c d e f g h i j k l m n o p q r s t u v w x y z");
    }

    @Test
    public void wrapTest_HOV_B_120_true_2() {
        runTest(120, true, 4,
                HOV(2, 0, "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"),
                "a  b  c  d  e  f  g  h  i  j  k  l  m  n  o  p  q  r  s  t  u  v  w  x  y  z");
    }

    @Test
    public void wrapTest_HOV_B_120_true_3() {
        runTest(120, true, 4,
                HOV(3, 0, "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"),
                "a   b   c   d   e   f   g   h   i   j   k   l   m   n   o   p   q   r   s   t   u   v   w   x   y   z");
    }

    @Test
    public void wrapTest_HOV_B_120_true_4() {
        runTest(120, true, 4,
                HOV(4, 0, "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"),
                "a\nb\nc\nd\ne\nf\ng\nh\ni\nj\nk\nl\nm\nn\no\np\nq\nr\ns\nt\nu\nv\nw\nx\ny\nz");
    }

    // ============================================================================================

    @Test
    public void wrapTest_HV_A_5_true_4_0() {
        runTest(5, true, 4,
                HV(0, 0, "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"),
                "abcde\n" + "fghij\n" +
                "klmno\n" + "pqrst\n" +
                "uvwxy\n" + "z");
    }

    @Test
    public void wrapTest_HV_A_5_true_4_1() {
        runTest(5, true, 4,
                HV(1, 0, "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"),
                "a b c\n" + "d e f\n" + "g h i\n" + "j k l\n" +
                "m n o\n" + "p q r\n" + "s t u\n" +
                "v w x\n" + "y z");
    }

    @Test
    public void wrapTest_HV_A_5_true_4_2() {
        runTest(5, true, 4,
                HV(2, 0, "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"),
                "a  b\n" + "c  d\n" + "e  f\n" + "g  h\n" + "i  j\n" + "k  l\n" +
                "m  n\n" + "o  p\n" + "q  r\n" + "s  t\n" + "u  v\n" +
                "w  x\n" + "y  z");
    }

    @Test
    public void wrapTest_HV_A_5_true_4_3() {
        runTest(5, true, 4,
                HV(3, 0, "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"),
                "a   b\n" + "c   d\n" + "e   f\n" + "g   h\n" + "i   j\n" + "k   l\n" +
                "m   n\n" + "o   p\n" + "q   r\n" + "s   t\n" + "u   v\n" +
                "w   x\n" + "y   z");
    }

    @Test
    public void wrapTest_HV_A_5_true_4_4() {
        runTest(5, true, 4,
                HV(4, 0, "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"),
                "a\n" + "b\n" + "c\n" + "d\n" + "e\n" + "f\n" + "g\n" + "h\n" + "i\n" + "j\n" + "k\n" + "l\n" +
                "m\n" + "n\n" + "o\n" + "p\n" + "q\n" + "r\n" + "s\n" + "t\n" + "u\n" + "v\n" +
                "w\n" + "x\n" + "y\n" + "z");
    }

    @Test
    public void wrapTest_HV_A_5_true_4_5() {
        runTest(5, true, 4,
                HV(5, 0, "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"),
                "a\n" + "b\n" + "c\n" + "d\n" + "e\n" + "f\n" + "g\n" + "h\n" + "i\n" + "j\n" + "k\n" + "l\n" +
                "m\n" + "n\n" + "o\n" + "p\n" + "q\n" + "r\n" + "s\n" + "t\n" + "u\n" + "v\n" +
                "w\n" + "x\n" + "y\n" + "z");
    }

    @Test
    public void wrapTest_HV_A_10_true_4_0() {
        runTest(10, true, 4,
                HV(0, 0, "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"),
                "abcdefghij\n" +
                "klmnopqrst\n" +
                "uvwxyz");
    }

    @Test
    public void wrapTest_HV_A_10_true_4_1() {
        runTest(10, true, 4,
                HV(1, 0, "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"),
                "a b c d e\n" +
                "f g h i j\n" +
                "k l m n o\n" +
                "p q r s t\n" +
                "u v w x y\n" +
                "z");
    }

    @Test
    public void wrapTest_HV_A_10_true_4_2() {
        runTest(10, true, 4,
                HV(2, 0, "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"),
                "a  b  c  d\n" +
                "e  f  g  h\n" +
                "i  j  k  l\n" +
                "m  n  o  p\n" +
                "q  r  s  t\n" +
                "u  v  w  x\n" +
                "y  z");
    }

    @Test
    public void wrapTest_HV_A_10_true_4_3() {
        runTest(10, true, 4,
                HV(3, 0, "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"),
                "a   b   c\n" +
                "d   e   f\n" +
                "g   h   i\n" +
                "j   k   l\n" +
                "m   n   o\n" +
                "p   q   r\n" +
                "s   t   u\n" +
                "v   w   x\n" +
                "y   z");
    }

    @Test
    public void wrapTest_HV_A_20_true_4_0() {
        runTest(20, true, 4,
                HV(0, 0, "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"),
                "abcdefghijklmnopqrst\n" +
                "uvwxyz");
    }

    // ============================================================================================

    @Test
    public void wrapTest_HV_B_20_true_4_0() {
        runTest(20, true, 4,
                HV(0, 0, "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd"),
                "abcdabcdabcdabcdabcd\n" +
                "abcdabcdabcdabcdabcd\n" +
                "abcdabcdabcdabcdabcd\n" +
                "abcd");
    }

    @Test
    public void wrapTest_HV_B_20_true_4_1() {
        runTest(20, true, 4,
                HV("abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd"),
                "abcd abcd abcd abcd\n" +
                "abcd abcd abcd abcd\n" +
                "abcd abcd abcd abcd\n" +
                "abcd abcd abcd abcd");
    }

    @Test
    public void wrapTest_HV_B_20_true_4_2() {
        runTest(20, true, 4,
                HV(2, 0, "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd"),
                "abcd  abcd  abcd\n" +
                "abcd  abcd  abcd\n" +
                "abcd  abcd  abcd\n" +
                "abcd  abcd  abcd\n" +
                "abcd  abcd  abcd\n" +
                "abcd");
    }

    @Test
    public void wrapTest_HV_B_20_true_4_3() {
        runTest(20, true, 4,
                HV(3, 0, "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd"),
                "abcd   abcd   abcd\n" +
                "abcd   abcd   abcd\n" +
                "abcd   abcd   abcd\n" +
                "abcd   abcd   abcd\n" +
                "abcd   abcd   abcd\n" +
                "abcd");
    }

    @Test
    public void wrapTest_HV_B_40_true_4_0() {
        runTest(40, true, 4,
                HV(0, 0, "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd"),
                "abcdabcdabcdabcdabcdabcdabcdabcdabcdabcd\n" +
                "abcdabcdabcdabcdabcdabcd");
    }

    @Test
    public void wrapTest_HV_B_40_true_4_1() {
        runTest(40, true, 4,
                HV("abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd"),
                "abcd abcd abcd abcd abcd abcd abcd abcd\n" +
                "abcd abcd abcd abcd abcd abcd abcd abcd");
    }

    @Test
    public void wrapTest_HV_B_40_true_4_2() {
        runTest(40, true, 4,
                HV(2, 0, "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd"),
                "abcd  abcd  abcd  abcd  abcd  abcd  abcd\n" +
                "abcd  abcd  abcd  abcd  abcd  abcd  abcd\n" +
                "abcd  abcd");
    }

    @Test
    public void wrapTest_HV_B_40_true_4_3() {
        runTest(40, true, 4,
                HV(3, 0, "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd"),
                "abcd   abcd   abcd   abcd   abcd   abcd\n" +
                "abcd   abcd   abcd   abcd   abcd   abcd\n" +
                "abcd   abcd   abcd   abcd");
    }

    @Test
    public void wrapTest_HV_B_60_true_4_0() {
        runTest(60, true, 4,
                HV(0, 0, "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd"),
                "abcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcd\n" +
                "abcd");
    }

    @Test
    public void wrapTest_HV_B_60_true_4_1() {
        runTest(60, true, 4,
                HV("abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd"),
                "abcd abcd abcd abcd abcd abcd abcd abcd abcd abcd abcd abcd\n" +
                "abcd abcd abcd abcd");
    }

    @Test
    public void wrapTest_HV_B_60_true_4_2() {
        runTest(60, true, 4,
                HV(2, 0, "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd"),
                "abcd  abcd  abcd  abcd  abcd  abcd  abcd  abcd  abcd  abcd\n" +
                "abcd  abcd  abcd  abcd  abcd  abcd");
    }

    @Test
    public void wrapTest_HV_B_60_true_4_3() {
        runTest(60, true, 4,
                HV(3, 0, "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd"),
                "abcd   abcd   abcd   abcd   abcd   abcd   abcd   abcd   abcd\n" +
                "abcd   abcd   abcd   abcd   abcd   abcd   abcd");
    }

    @Test
    public void wrapTest_HV_B_80_true_4_0() {
        runTest(80, true, 4,
                HV(0, 0, "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd"),
                "abcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcd");
    }

    @Test
    public void wrapTest_HV_B_80_true_4_1() {
        runTest(80, true, 4,
                HV("abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd"),
                "abcd abcd abcd abcd abcd abcd abcd abcd abcd abcd abcd abcd abcd abcd abcd abcd");
    }

    @Test
    public void wrapTest_HV_B_80_true_4_2() {
        runTest(80, true, 4,
                HV(2, 0, "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd"),
                "abcd  abcd  abcd  abcd  abcd  abcd  abcd  abcd  abcd  abcd  abcd  abcd  abcd\n" +
                "abcd  abcd  abcd");
    }

    @Test
    public void wrapTest_HV_B_80_true_4_3() {
        runTest(80, true, 4,
                HV(3, 0, "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd"),
                "abcd   abcd   abcd   abcd   abcd   abcd   abcd   abcd   abcd   abcd   abcd\n" +
                "abcd   abcd   abcd   abcd   abcd");
    }

    @Test
    public void wrapTest_HV_B_100_true_4_0() {
        runTest(100, true, 4,
                HV(0, 0, "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd"),
                "abcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcd");
    }

    @Test
    public void wrapTest_HV_B_100_true_4_1() {
        runTest(100, true, 4,
                HV("abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd"),
                "abcd abcd abcd abcd abcd abcd abcd abcd abcd abcd abcd abcd abcd abcd abcd abcd");
    }

    @Test
    public void wrapTest_HV_B_100_true_4_2() {
        runTest(100, true, 4,
                HV(2, 0, "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd"),
                "abcd  abcd  abcd  abcd  abcd  abcd  abcd  abcd  abcd  abcd  abcd  abcd  abcd  abcd  abcd  abcd");
    }

    @Test
    public void wrapTest_HV_B_100_true_4_3() {
        runTest(100, true, 4,
                HV(3, 0, "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd"),
                "abcd   abcd   abcd   abcd   abcd   abcd   abcd   abcd   abcd   abcd   abcd   abcd   abcd   abcd\n" +
                "abcd   abcd");
    }

    @Test
    public void wrapTest_HV_B_120_true_4_0() {
        runTest(120, true, 4,
                HV(0, 0, "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd"),
                "abcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcd");
    }

    @Test
    public void wrapTest_HV_B_120_true_4_1() {
        runTest(120, true, 4,
                HV("abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd"),
                "abcd abcd abcd abcd abcd abcd abcd abcd abcd abcd abcd abcd abcd abcd abcd abcd");
    }

    @Test
    public void wrapTest_HV_B_120_true_4_2() {
        runTest(120, true, 4,
                HV(2, 0, "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd"),
                "abcd  abcd  abcd  abcd  abcd  abcd  abcd  abcd  abcd  abcd  abcd  abcd  abcd  abcd  abcd  abcd");
    }

    @Test
    public void wrapTest_HV_B_120_true_4_3() {
        runTest(120, true, 4,
                HV(3, 0, "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd"),
                "abcd   abcd   abcd   abcd   abcd   abcd   abcd   abcd   abcd   abcd   abcd   abcd   abcd   abcd   abcd   abcd");
    }

    // ============================================================================================

    @Test
    public void indentTest1() {
        runTest(20, true, 4,
                I(1, "a"),
                " a");
    }

    @Test
    public void indentTest2() {
        runTest(20, true, 4,
                I(2, "a"),
                "  a");
    }

    @Test
    public void indentTest3() {
        runTest(20, true, 4,
                I(3, "a"),
                "   a");
    }

    @Test
    public void indentTest4() {
        runTest(20, true, 4,
                I("a"),
                "    a");
    }

    // ============================================================================================

    @Test
    public void composeHH_1() {
        runTest(20, true, 4,
                H( H("a", "b", "c"), H("d", "e", "f"), H("g", "h", "i")),
                "a b c d e f g h i");
    }

    @Test
    public void composeVV_1() {
        runTest(20, true, 4,
                V( V("a", "b", "c"), V("d", "e", "f"), V("g", "h", "i")),
                "a\nb\nc\nd\ne\nf\ng\nh\ni");
    }

    @Test
    public void composeVH_1() {
        runTest(20, true, 4,
                V( H("a", "b", "c"), H("d", "e", "f"), H("g", "h", "i")),
                "a b c\nd e f\ng h i");
    }

    @Test
    public void composeHV_1() {
        runTest(20, true, 4,
                H( V("a", "b", "c"), V("d", "e", "f"), V("g", "h", "i")),
                "a\nb\nc d\n  e\n  f g\n    h\n    i");
    }

    @Test
    public void composeHV_2() {
        runTest(20, true, 4,
                H(2, V("a", "b", "c"), V("d", "e", "f"), V("g", "h", "i")),
                "a\nb\nc  d\n   e\n   f  g\n      h\n      i");
    }

    @Test
    public void composeHV_3() {
        runTest(20, true, 4,
                H( V("aaa", "bbb", "ccc"), V("ddd", "eee", "fff"), V("ggg", "hhh", "iii")),
                "aaa\nbbb\nccc ddd\n    eee\n    fff ggg\n        hhh\n        iii");
    }

    @Test
    public void composeHV_4() {
        runTest(20, true, 4,
                H( V("aaa", "bbb", "ccc"), V("dd", "ee", "ff"), V("g", "h", "i")),
                "aaa\nbbb\nccc dd\n    ee\n    ff g\n       h\n       i");
    }

    // ============================================================================================

    @Test
    public void methodTest1() {
        runTest(80, true, 4,
                V(H("public", "def", H(0, "foo", "(", HV("x: int,", "y: int,", "sa: Array[String]"), ")"), "{"),
                      I("Console.OUT.println(Hello, World!);"),
                  "}"),
                "public def foo(x: int, y: int, sa: Array[String]) {\n" +
                "    Console.OUT.println(Hello, World!);\n" +
                "}");
    }

    @Test
    public void methodTest2() {
        runTest(80, true, 4,
                V(H("public", "def", H(0, "foo", "(", HV(H("x", ":", "int,"), H("y", ":", "int,"), H("sa", ":", "Array[String]")), ")"), "{"),
                      I("Console.OUT.println(Hello, World!);"),
                  "}"),
                "public def foo(x : int, y : int, sa : Array[String]) {\n" +
                "    Console.OUT.println(Hello, World!);\n" +
                "}");
    }

    private static void runTest(int pageWidth, boolean spacesForTabs, int tabWidth, String boxString, String expected) {
        BoxInterpreter bi= new BoxInterpreter(pageWidth, true, 4);
        IBox boxPrg= parseBox(boxString);
        String actual= bi.interpret(boxPrg);
        boolean result= actual != null && actual.equals(expected);

        if (!result) {
            System.out.println(boxString);
            System.out.println("Actual:");
            System.out.println(actual);
            System.out.println("Expected:");
            System.out.println(expected);
        }
        assertEquals(expected, actual);
    }

    @Test
    public void bigTest() {
        runFileTest("data/KMeans-x10.box", "data/KMeans.x10");
    }

    private static void runFileTest(String inputFilePath, String expectedFilePath) {
        File inputFile= new File(inputFilePath);
        File expectedFile= new File(expectedFilePath);

        String input= readFile(inputFile);
        String expected= readFile(expectedFile);

        BoxInterpreter bi= new BoxInterpreter(80, true, 4);
        IBox boxPrg= parseBox(input);
        String actual= bi.interpret(boxPrg);

        assertEquals(expected, actual);
    }

    private static String readFile(File file) {
        try {
            FileReader fileReader= new FileReader(file);
            char[] buf= new char[(int) file.length()];
            int len= fileReader.read(buf);

            assertTrue(len > 0);
            return new String(buf);
        } catch (IOException e) {
            assertTrue(e.getMessage(), false);
            return "";
        }
    }

    private static IBox parseBox(String boxString) {
        BoxParseController bpc= new BoxParseController(true);
        bpc.initialize(null, null, new SystemOutErrMessageHandler());
        IBox box= (IBox) bpc.parse(boxString, null);
        return box;
    }

    // ============================================================================

    public static String H(String... args) {
        return emitOp("H", null, args);
    }

    public static String H(int horizSpace, String... args) {
        return emitOp("H", "hs=" + horizSpace, args);
    }

    public static String V(String... args) {
        return emitOp("V", null, args);
    }

    public static String V(int vertSpace, String... args) {
        return emitOp("V", "vs=" + vertSpace, args);
    }

    public static String I(String... args) {
        return emitOp("I", null, args);
    }

    public static String I(int indent, String... args) {
        return emitOp("I", "is=" + indent, args);
    }

    public static String HV(String... args) {
        return emitOp("HV", null, args);
    }

    public static String HV(int horizSpace, int vertSpace, String... args) {
        return emitOp("HV", "hs=" + horizSpace + " vs=" + vertSpace, args);
    }

    public static String HOV(String... args) {
        return emitOp("HOV", null, args);
    }

    public static String HOV(int horizSpace, int vertSpace, String... args) {
        return emitOp("HOV", "hs=" + horizSpace + " vs=" + vertSpace, args);
    }

    private static String emitOp(String operator, String options, String[] args) {
        StringBuffer sb= new StringBuffer();
        sb.append(operator);
        if (options != null) {
            sb.append(' ');
            sb.append(options);
            sb.append(' ');
        }
        sb.append("[ ");
        for(String arg : args) {
            boolean isLiteralArg= !(arg.startsWith("H[") || arg.startsWith("H hs=") || arg.startsWith("V[") || arg.startsWith("V vs=") || arg.startsWith("I[") || arg.startsWith("I is=")
                            || arg.startsWith("HV[") || arg.startsWith("HV hs=") || arg.startsWith("HOV[") || arg.startsWith("HOV hs="));
            String escapedArg= arg;
            if (isLiteralArg) {
                sb.append("\"");
                escapedArg= arg.replace("\"", "\\\""); // only escape once, at the literal leaf nodes
            }
            sb.append(escapedArg);
            if (isLiteralArg) {
                sb.append("\"");
            }
            sb.append(' ');
        }
        sb.append("]");
        return sb.toString();
    }
}
