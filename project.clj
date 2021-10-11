(defproject algorithm "0.1.0-SNAPSHOT"
  :description "Parallel Asynchronous Particle Swarm Optimization"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [quil "3.1.0"]]
  :dev-dependencies [[speclj "3.3.1"]]
  :repl-options {:init-ns algorithm.core}
  :main benchmark.scaling
  :aot [benchmark.scaling])
