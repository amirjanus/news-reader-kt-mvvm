package com.example.newsreader.utils.containers

/**
 * Data structure that holds two values.
 *
 * @param <F> First object.
 * @param <S> Second object.
 */
class Pair<F, S>(first: F, second: S) {

    var first: F = first
    var second: S = second

    /**
     * Returns a string representation of the object.
     */
    override fun toString(): String = "$this: $first, $second"

}
