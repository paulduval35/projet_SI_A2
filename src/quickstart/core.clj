(ns quickstart.core
  (:gen-class)
  (:require
     [pallet.api :refer [group-spec server-spec node-spec plan-fn]]
     [pallet.crate.automated-admin-user :refer [automated-admin-user]]
     [pallet.actions :refer [exec-script* exec-script]]
     [pallet.ssh.execute :refer [ssh-connection]]
     [pallet.crate :refer [defplan]]
  )
)
 
 
 
(def aws (pallet.configure/compute-service :aws))
(defplan install-boot
  []
  (exec-script* "apt-get install -y apache2 apache2-doc apache2-mpm-prefork apache2-utils libexpat1 ssl-cert")
  (exec-script* "apt-get install -y libapache2-mod-php5 php5 php5-common php5-curl php5-dev php5-gd php5-idn php-pear php5-imagick php5-imap php5-json php5-mcrypt php5-memcache php5-mhash php5-ming php5-mysql php5-ps php5-pspell php5-recode php5-snmp php5-sqlite php5-tidy php5-xmlrpc php5-xsl")
  (exec-script* "chmod 777 /var/www")
  (exec-script* "wget https://wordpress.org/wordpress-5.1.1.zip")
  (exec-script* "apt-get install -y unzip")
  (exec-script* "unzip -o wordpress-5.1.1.zip -d /var/www")
  (exec-script* "service  apache2 reload")
  
)
(def antoine 
  (pallet.api/group-spec "antoine"
    :node-spec (pallet.api/node-spec
               :image {:os-family :ubuntu 
                       :image-id "us-east-1/ami-3c994355"}
	       :network {:inbound-ports [22 80]})
    :phases {:bootstrap automated-admin-user
       :run (pallet.api/plan-fn
                       (install-boot))}
       :default-phases [:run]))

             
         

(def s (pallet.api/converge {antoine 1} :compute aws))


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
    
 (println "Hello world1")
)

