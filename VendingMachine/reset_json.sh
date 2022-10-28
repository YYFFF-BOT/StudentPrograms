dir="./app/src/main/resources"

for db in 'accounts' 'cash_change' 'credit_cards' 'products'
do
    cat "$dir/$db-default.json" > "$dir/$db.json"
done
