(ns pcp.scgi-test
  (:require [clojure.test :refer [deftest is testing]]
            [pcp.scgi :as scgi]
            [pcp.core :as core]
            [clojure.java.io :as io])
  (:import  [java.net Socket InetAddress]
            [org.apache.commons.io IOUtils]))

(def wait-time 500)

(deftest serve-test
  (testing "Test SCGI server"
    (let [handler #(core/scgi-handler %)
          scgi-port 55555
          server (scgi/serve handler scgi-port)
          message (IOUtils/toByteArray (io/input-stream "test-resources/scgi.bin"))
          len (count message)]
      (Thread/sleep wait-time)
      (let [socket (Socket. (InetAddress/getByName "127.0.0.1") scgi-port)]
        (with-open [os (io/output-stream (.getOutputStream socket))]
          (.write os message 0 len)
          (.flush os)
          (let [ans (IOUtils/toString (.getInputStream socket))
                _ (.close socket)]
            (is (= "200\r\nContent-Type: text/plain\r\n\r\n1275" ans))
            (is (true? (.isClosed socket)))
            (server)))))))

(deftest serve-2-test
  (testing "Test SCGI server"
    (let [handler #(core/scgi-handler %)
          scgi-port 11112
          server (scgi/serve handler scgi-port)
          message (IOUtils/toByteArray (io/input-stream "test-resources/multipart.bin"))
          len (count message)]
      (Thread/sleep wait-time)
      (let [socket (Socket. (InetAddress/getByName "127.0.0.1") scgi-port)]
        (with-open [os (io/output-stream (.getOutputStream socket))]
          (.write os message 0 len)
          (.flush os)
          (let [ans (IOUtils/toString (.getInputStream socket))]
            (is (= "200\r\nContent-Type: text/plain\r\n\r\n1275" ans))
            (Thread/sleep wait-time)
            (.close socket)
            (is (true? (.isClosed socket)))
            (server)))))))

(deftest serve-3-test
  (testing "Test SCGI server"
    (let [handler #(core/scgi-handler %)
          scgi-port 11113
          server (scgi/serve handler scgi-port)
          message (IOUtils/toByteArray (io/input-stream "test-resources/json.bin"))
          len (count message)]
      (Thread/sleep wait-time)
      (let [socket (Socket. (InetAddress/getByName "127.0.0.1") scgi-port)]
        (with-open [os (io/output-stream (.getOutputStream socket))]
          (.write os message 0 len)
          (.flush os)
          (let [ans (IOUtils/toString (.getInputStream socket))
                _ (.close socket)]
            (is (= "200\r\nContent-Type: text/plain\r\n\r\n1275" ans))
            (is (true? (.isClosed socket)))
            (server)))))))            