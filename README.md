Setup V2RayNG:

git clone --recursive https://github.com/2dust/v2rayNG.git

\***\* Change rxpermissions from 0.12 to v0.12 \*\***

$ANDROID_HOME/ndk

cd AndroidLibXrayLite
CURRENT_TAG=$(git describe --tags --abbrev=0)
echo "Downloading libv2ray for tag $CURRENT_TAG"
curl -L -o libv2ray.aar https://github.com/2dust/AndroidLibXrayLite/releases/download/$CURRENT_TAG/libv2ray.aar
mkdir ../V2rayNG/app/libs/
mv libv2ray.aar ../V2rayNG/app/libs/

cd ..
bash compile-tun2socks.sh
tar -xvzf libtun2socks.so.tgz
cp -r libs/\* V2rayNG/app/libs/
rm -rf libs
