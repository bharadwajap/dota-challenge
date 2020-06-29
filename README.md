bayes-dota
==========

This is the [task](TASK.md).

Any additional information about your solution goes here.



```
SELECT hero, count(1) FROM EVENT
where event_type = 'killed'
and hero like '%hero%'
group by hero
```

```
SELECT event_target, timestamp FROM EVENT
where event_type = 'buys'
and hero = 'npc_dota_hero_puck'
```

```
SELECT spell, count(1) FROM EVENT
where event_type = 'casts'
and hero = 'npc_dota_hero_pangolier'
group by spell

```

```
SELECT event_target,  count(1), sum(damage) FROM EVENT
where event_type = 'hits'
and hero = 'npc_dota_hero_abyssal_underlord'
group by event_target
```