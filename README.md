# markov-aurelius

Markov chain modeling the distributions of words in Marcus Aurelius' [Meditations](http://classics.mit.edu/Antoninus/meditations.html).

[re-frame](https://github.com/Day8/re-frame) is used for the webapp.

Data isn't checked in, because of the file size.

Create a `data.cljs` at `./src/cljs/markov_aurelius/data.cljs` with contents:

```clojure
(ns markov-aurelius.data)

(def data
  "blah blah [...] blah")
```

## Development Mode

### Run application:

```
lein clean
lein figwheel dev
```

Figwheel will automatically push cljs changes to the browser.

Wait a bit, then browse to [http://localhost:3449](http://localhost:3449).

## Production Build


To compile clojurescript to javascript:

```
lein clean
lein cljsbuild once min
```
