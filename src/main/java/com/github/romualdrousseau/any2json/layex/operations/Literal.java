package com.github.romualdrousseau.any2json.layex.operations;

import com.github.romualdrousseau.any2json.base.Symbol;
import com.github.romualdrousseau.any2json.layex.TableParser;
import com.github.romualdrousseau.any2json.layex.Lexer;
import com.github.romualdrousseau.any2json.layex.TableMatcher;

public class Literal implements TableMatcher {

    public Literal(final String v) {
        this.v = v;
    }

    @Override
    public <S extends Symbol, C> boolean match(final Lexer<S, C> stream, final TableParser<S> context) {
        final S symbol = stream.read();
        final String c = symbol.getSymbol();
        if (!c.equals("") && c.charAt(0) >= 'a' && c.charAt(0) <= 'z' && c.equals("e") && symbol.matchLiteral(this.v)) {
            if (context != null) {
                context.notify(symbol);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "LITERAL('" + this.v + "')";
    }

    private final String v;
}
