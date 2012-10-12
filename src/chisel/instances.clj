;; See README for license information
(ns chisel.instances
  "Convert docID->text hashmap to MALLET input format (InstanceList)"
  (:import (cc.mallet.types InstanceList Instance))
  (:import (cc.mallet.pipe Pipe
                           SerialPipes
                           CharSequence2TokenSequence
                           TokenSequence2FeatureSequence
                           StringList2FeatureSequence)))
                                  
(defn document-to-instance
  "Convert a document ID and a text string to a MALLET instance"
  [[documentid text]]
  (Instance. text "nolabel" documentid nil))

(defn tokens-to-instance
  "Convert a document ID and a text string to a MALLET instance"
  [[documentid text]]
  (Instance. (java.util.ArrayList. text) "nolabel" documentid nil))

(defn get-instance-list
  "Convert (documentID, text) map to MALLET InstanceList"
  [documentmap]
  (let [pipes (new SerialPipes                
                   [(new CharSequence2TokenSequence #"[\p{L}\p{P}]*\p{L}")
                    (new TokenSequence2FeatureSequence)])]                    
    (doto (new InstanceList pipes)
      (.addThruPipe (.iterator (map document-to-instance documentmap)))))) 

(defn get-instance-list-from-iter
  "Convert (documentID, text) map to MALLET InstanceList"
  [instanceiter]
  (let [pipes (new SerialPipes                
                   [(new CharSequence2TokenSequence #"[\p{L}\p{P}]*\p{L}")
                    (new TokenSequence2FeatureSequence)])]                    
    (doto (new InstanceList pipes)
      (.addThruPipe (.iterator instanceiter)))))

(defn get-instance-list-from-tokens
  "Convert (documentID, [\"tokens\"]) map to MALLET InstanceList"
  [documentmap]
  (let [pipe (new StringList2FeatureSequence)]
    (doto (new InstanceList pipe)
      (.addThruPipe (.iterator (map tokens-to-instance documentmap))))))
