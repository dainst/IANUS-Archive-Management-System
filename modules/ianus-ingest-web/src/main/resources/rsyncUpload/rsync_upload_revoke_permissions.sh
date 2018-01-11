#!/bin/bash

# This script needs to be owned by root, 
# in order to be executable by any other user using sudo.
# The sudoer will inherit the permissions of the owner / group-owner while running the script.

# sudo chown root:root rsync_upload_revoke_permissions.sh
# sudo chmod 774 rsync_upload_revoke_permissions.sh

# In sudoers file (edit with visudo!), enable tomcat to call this script with root privileges 
# without password: 
# $ sudo visudo -f /etc/sudoers.d/rsync_upload
# insert: 
# tomcat8 ALL=(ALL) NOPASSWD: /var/lib/tomcat8/webapps/ianus-ingest-web/WEB-INF/classes/rsyncUpload/rsync_upload$
# tomcat8 ALL=(ALL) NOPASSWD: /var/lib/tomcat8/webapps/ianus-ingest-web/WEB-INF/classes/rsyncUpload/rsync_upload$


# test (fail): 
# ./rsync_upload_revoke_permissions.sh --storage-path=/foo --ip-root=/89 --ip-target=/some/path --username=hschmeer

# test (succeed):
# ./rsync_upload_revoke_permissions.sh --storage-path=/foo/ --ip-root=tp_2017_89_0000128937 --ip-target=/some/path --username=hschmeer --upload-id=3


# get the parameters...

for parameter in "$@"
do
	case $parameter in
		
		--storage-path=*)
		storagePath="${parameter#*=}"
		shift
		;;
		
		--ip-root=*)
		ipRoot="${parameter#*=}"
		shift
		;;
		
		--ip-target=*)
		ipTarget="${parameter#*=}"
		shift
		;;
		
		--username=*)
		username="${parameter#*=}"
		shift
		;;
		
		--upload-id=*)
		uploadId="${parameter#*=}"
		shift
		;;
		
		*)
		# unknown option
		;;
	esac
done


# check for mandatory parameters
if [[ -z $storagePath ]] || [[ -z $ipRoot ]] || [[ -z $ipTarget ]] || [[ -z $username ]] || [[ -z $uploadId ]]
then
	(>&2 echo "missing parameter, exiting")
	exit 1
fi

# check parameter format

if [[ ! $storagePath =~ ^/.+/$ ]]
then
	(>&2 echo "bad parameter storage-path, exiting")
	exit 1
fi

if [[ ! $ipRoot =~ ^tp_[0-9]{4}_[0-9]{2}_[0-9]+|^sip_[0-9]{4}_[0-9]{2}_[0-9]+[^/]$ ]]
then
	(>&2 echo "bad parameter ip-root, exiting")
	exit 1
fi

if [[ ! $ipTarget =~ ^/.+[^/]$ ]]
then
	(>&2 echo "bad parameter ipTarget, exiting")
	exit 1
fi

if [[ ! $uploadId =~ ^[0-9]+$ ]]
then
	(>&2 echo "bad parameter upload-id, exiting")
	exit 1
fi





absolutePath="${storagePath}${ipRoot}${ipTarget}"

groupname="tmp_${uploadId}_${username}"


echo "Revoking permissions for user ${username} at location ${absolutePath}"
echo "Removing temporary group ${groupname}, control taken over by tomcat8"

chgrp -R tomcat8 $absolutePath
chmod -R 755 $absolutePath
deluser $username $groupname
#gpasswd -d $username $groupname
groupdel $groupname



