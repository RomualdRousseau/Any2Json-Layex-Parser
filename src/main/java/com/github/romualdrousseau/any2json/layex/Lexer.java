package com.github.romualdrousseau.any2json.layex;

import com.github.romualdrousseau.any2json.base.Symbol;

public interface Lexer<S extends Symbol, C> {

    S read();

    S peek();

    void push();

    C pop();

    void seek(C i);
  }
