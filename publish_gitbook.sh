gitbook build

cp -R _book/* .
rm -r _book/

git ciam "update"
git push origin master
